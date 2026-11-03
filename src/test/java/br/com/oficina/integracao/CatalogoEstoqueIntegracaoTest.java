package br.com.oficina.integracao;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CatalogoEstoqueIntegracaoTest extends IntegracaoTest {

    @Test
    void cicloDeVidaDoServico() throws Exception {
        String admin = tokenAdmin();
        String nome = "Alinhamento " + unico();
        ResultActions criado = post("/servicos", admin, """
                {"nome": "%s", "descricao": "Alinhamento 3D", "precoBase": "80.00"}""".formatted(nome))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.precoBase").value("80.00"));
        Integer id = ler(criado, "$.id");
        ClienteCriado cliente = novoCliente();

        get("/servicos/" + id, cliente.token()).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value(nome));
        get("/servicos?nome=" + nome.substring(12), cliente.token())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
        put("/servicos/" + id, admin, """
                {"nome": "%s", "descricao": "Alinhamento e balanceamento", "precoBase": "95.50"}""".formatted(nome))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precoBase").value("95.50"));
        delete("/servicos/" + id, admin).andExpect(status().isNoContent());
        get("/servicos/" + id, admin).andExpect(status().isNotFound());
    }

    @Test
    void regrasDoCatalogo() throws Exception {
        String admin = tokenAdmin();
        String mecanico = novoMecanico(admin);

        post("/servicos", mecanico, """
                {"nome": "Balanceamento", "precoBase": "60.00"}""").andExpect(status().isForbidden());
        post("/servicos", admin, """
                {"nome": "Balanceamento", "precoBase": "60,5"}""").andExpect(status().isBadRequest());
        post("/servicos", admin, """
                {"nome": " ", "precoBase": "60.00"}""").andExpect(status().isBadRequest());
        put("/servicos/999999", admin, """
                {"nome": "Inexistente", "precoBase": "10.00"}""").andExpect(status().isNotFound());
        delete("/servicos/999999", admin).andExpect(status().isNotFound());
    }

    @Test
    void cicloDeVidaDaPeca() throws Exception {
        String admin = tokenAdmin();
        String mecanico = novoMecanico(admin);
        String sku = "FLT-" + unico();
        ResultActions criada = post("/pecas", mecanico, """
                {"sku": "%s", "nome": "Filtro de óleo", "unidadeMedida": "UN", "precoCusto": "20.00",
                 "precoVenda": "35.50", "estoqueMinimo": 5}""".formatted(sku))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.unidadeMedida").value("UN"))
                .andExpect(jsonPath("$.abaixoDoMinimo").value(true));
        Integer id = ler(criada, "$.id");

        movimentar(mecanico, id, "ENTRADA", "20").andExpect(jsonPath("$.quantidadeEstoque").value(20));
        movimentar(mecanico, id, "SAIDA", "2").andExpect(jsonPath("$.quantidadeEstoque").value(18));
        movimentar(mecanico, id, "AJUSTE_INVENTARIO", "3").andExpect(jsonPath("$.quantidadeEstoque").value(3));
        get("/pecas/" + id, mecanico).andExpect(status().isOk()).andExpect(jsonPath("$.abaixoDoMinimo").value(true));
        get("/pecas?abaixoDoMinimo=true&size=100", mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(1)));
        get("/pecas?nome=Filtro", mecanico).andExpect(status().isOk());
        put("/pecas/" + id, mecanico, """
                {"sku": "%s", "nome": "Filtro de óleo Mann", "unidadeMedida": "UN", "precoCusto": "21.00",
                 "precoVenda": "37.00", "estoqueMinimo": 1}""".formatted(sku))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Filtro de óleo Mann"))
                .andExpect(jsonPath("$.abaixoDoMinimo").value(false));

        delete("/pecas/" + id, mecanico).andExpect(status().isForbidden());
        delete("/pecas/" + id, admin).andExpect(status().isNoContent());
        get("/pecas/" + id, admin).andExpect(status().isNotFound());
    }

    @Test
    void regrasDoEstoque() throws Exception {
        String admin = tokenAdmin();
        Integer filtro = novaPeca(admin, "UN", 1);
        Integer oleo = novaPeca(admin, "LT", 0);

        movimentar(admin, filtro, "SAIDA", "2")
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("ESTOQUE_INSUFICIENTE"));
        movimentar(admin, filtro, "SAIDA", "0.5")
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("QUANTIDADE_FRACIONADA"));
        movimentar(admin, oleo, "ENTRADA", "10.5").andExpect(jsonPath("$.quantidadeEstoque").value(10.5));
        movimentar(admin, oleo, "SAIDA", "3.75").andExpect(jsonPath("$.quantidadeEstoque").value(6.75));
        movimentar(admin, 999999, "ENTRADA", "1").andExpect(status().isNotFound());
        get("/pecas", novoCliente().token()).andExpect(status().isForbidden());
    }

    @Test
    void recusaSkuDuplicadoEUnidadeInvalida() throws Exception {
        String admin = tokenAdmin();
        String sku = "DUP-" + unico();
        String corpo = """
                {"sku": "%s", "nome": "Pastilha", "unidadeMedida": "%s", "precoCusto": "50.00",
                 "precoVenda": "90.00", "estoqueMinimo": %s}""";
        post("/pecas", admin, corpo.formatted(sku, "JG", "1")).andExpect(status().isCreated());

        post("/pecas", admin, corpo.formatted(sku, "JG", "1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("SKU_JA_CADASTRADO"));
        post("/pecas", admin, corpo.formatted("UN-" + unico(), "UN", "1.5"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("QUANTIDADE_FRACIONADA"));
    }

    private ResultActions movimentar(String token, Integer pecaId, String tipo, String quantidade) throws Exception {
        return post("/pecas/" + pecaId + "/movimentacoes", token, """
                {"tipo": "%s", "quantidade": %s}""".formatted(tipo, quantidade));
    }
}
