package br.com.oficina.integracao;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastroIntegracaoTest extends IntegracaoTest {

    @Test
    void clienteSeCadastraEConsultaEAtualizaOProprioCadastro() throws Exception {
        String documento = cpf();
        String email = "joao." + unico() + "@email.com";
        ResultActions criado = post("/clientes", null, """
                {"documento": "%s", "nome": "João da Silva", "email": "%s", "telefone": "+5511988887777",
                 "senha": "senhaCliente1"}""".formatted(documento, email))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.tipoDocumento").value("CPF"))
                .andExpect(jsonPath("$.documento").value(documento))
                .andExpect(jsonPath("$.nome").value("João da Silva"));
        Integer id = ler(criado, "$.id");
        String cliente = login(email, "senhaCliente1");

        get("/clientes/" + id, cliente).andExpect(status().isOk()).andExpect(jsonPath("$.email").value(email));
        put("/clientes/" + id, cliente, """
                {"nome": "João Silva", "email": "%s", "telefone": "+5511977776666"}""".formatted(email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.telefone").value("+5511977776666"));
        post("/clientes/pesquisa", tokenAdmin(), """
                {"documento": "%s"}""".formatted(documento))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void mecanicoCadastraNoBalcaoEListaPorNome() throws Exception {
        String mecanico = novoMecanico(tokenAdmin());
        String nome = "Maria " + unico();
        post("/clientes", mecanico, """
                {"documento": "%s", "nome": "%s", "email": "maria.%s@email.com", "telefone": "+5511966665555",
                 "senha": "senhaCliente2"}""".formatted(cpf(), nome, unico()))
                .andExpect(status().isCreated());

        get("/clientes?nome=" + nome.substring(6), mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nome").value(nome));
        get("/clientes?page=0&size=5", mecanico).andExpect(status().isOk());
    }

    @Test
    void clienteNaoVeNemCadastraOutrosClientes() throws Exception {
        ClienteCriado joao = novoCliente();
        ClienteCriado maria = novoCliente();

        get("/clientes/" + maria.id(), joao.token())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RECURSO_NAO_ENCONTRADO"));
        get("/clientes", joao.token()).andExpect(status().isForbidden());
        post("/clientes", joao.token(), """
                {"documento": "%s", "nome": "Outro", "email": "o.%s@email.com", "telefone": "+5511988887777",
                 "senha": "senhaCliente1"}""".formatted(cpf(), unico()))
                .andExpect(status().isForbidden());
    }

    @Test
    void recusaDocumentoDuplicadoOuInvalido() throws Exception {
        String documento = cpf();
        String corpo = """
                {"documento": "%s", "nome": "Ana", "email": "%s@email.com", "telefone": "+5511988887777",
                 "senha": "senhaCliente1"}""";
        post("/clientes", null, corpo.formatted(documento, "ana." + unico())).andExpect(status().isCreated());

        post("/clientes", null, corpo.formatted(documento, "ana2." + unico()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("DOCUMENTO_JA_CADASTRADO"));
        post("/clientes", null, corpo.formatted("52998224700", "ana3." + unico()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACAO"));
        post("/clientes/pesquisa", tokenAdmin(), """
                {"documento": "%s"}""".formatted(cpf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void cicloDeVidaDoVeiculo() throws Exception {
        String admin = tokenAdmin();
        String mecanico = novoMecanico(admin);
        ClienteCriado cliente = novoCliente();
        String placa = placa();
        ResultActions criado = post("/veiculos", cliente.token(), """
                {"clienteId": %d, "placa": "%s", "marca": "Volkswagen", "modelo": "Gol 1.6", "ano": 2019,
                 "cor": "Prata", "chassi": "9BWZZZ377VT%06d", "quilometragem": 85000}"""
                .formatted(cliente.id(), placa, Math.abs(unico().hashCode()) % 1_000_000))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value(placa));
        Integer veiculoId = ler(criado, "$.id");

        get("/veiculos", cliente.token())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[*].clienteId", everyItem(is(cliente.id()))));
        get("/veiculos?placa=" + placa, mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
        get("/veiculos?clienteId=" + cliente.id(), mecanico).andExpect(status().isOk());
        put("/veiculos/" + veiculoId, mecanico, """
                {"clienteId": %d, "placa": "%s", "marca": "Volkswagen", "modelo": "Gol 1.6", "ano": 2019,
                 "cor": "Preto", "quilometragem": 90000}""".formatted(cliente.id(), placa))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quilometragem").value(90000))
                .andExpect(jsonPath("$.cor").value("Preto"));
        get("/veiculos/" + veiculoId, cliente.token()).andExpect(status().isOk());

        delete("/veiculos/" + veiculoId, mecanico).andExpect(status().isForbidden());
        delete("/veiculos/" + veiculoId, admin).andExpect(status().isNoContent());
        get("/veiculos/" + veiculoId, admin).andExpect(status().isNotFound());
    }

    @Test
    void regrasDoVeiculo() throws Exception {
        String admin = tokenAdmin();
        ClienteCriado joao = novoCliente();
        ClienteCriado maria = novoCliente();
        Integer veiculoMaria = novoVeiculo(maria.id(), admin);
        String placa = placa();
        String corpo = """
                {"clienteId": %d, "placa": "%s", "marca": "Fiat", "modelo": "Uno", "ano": %d}""";
        post("/veiculos", joao.token(), corpo.formatted(joao.id(), placa, 2010)).andExpect(status().isCreated());

        post("/veiculos", admin, corpo.formatted(joao.id(), placa, 2010))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("PLACA_JA_CADASTRADA"));
        post("/veiculos", joao.token(), corpo.formatted(maria.id(), placa(), 2010)).andExpect(status().isForbidden());
        get("/veiculos/" + veiculoMaria, joao.token()).andExpect(status().isNotFound());
        post("/veiculos", admin, corpo.formatted(joao.id(), placa(), 1900)).andExpect(status().isBadRequest());
        post("/veiculos", admin, corpo.formatted(999999, placa(), 2010)).andExpect(status().isNotFound());
    }

    @Test
    void adminRemoveClienteSemVinculos() throws Exception {
        String admin = tokenAdmin();
        ClienteCriado cliente = novoCliente();

        delete("/clientes/" + cliente.id(), novoMecanico(admin)).andExpect(status().isForbidden());
        delete("/clientes/" + cliente.id(), admin).andExpect(status().isNoContent());
        get("/clientes/" + cliente.id(), admin).andExpect(status().isNotFound());
        delete("/clientes/" + cliente.id(), admin).andExpect(status().isNotFound());
    }

    @Test
    void clienteComVeiculoNaoPodeSerRemovido() throws Exception {
        String admin = tokenAdmin();
        ClienteCriado cliente = novoCliente();
        novoVeiculo(cliente.id(), admin);

        delete("/clientes/" + cliente.id(), admin)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("REGISTRO_EM_USO"));
        get("/clientes?size=100", admin)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(1)));
    }
}
