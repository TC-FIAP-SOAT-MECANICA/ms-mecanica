package br.com.oficina.integracao;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:oficina-it;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;"
                + "DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1",
        "spring.h2.console.enabled=false"
})
abstract class IntegracaoTest {

    private static final AtomicInteger SEQUENCIA = new AtomicInteger();
    private static final String LETRAS = "ABCDEFGHJKLMNPRSTUVWXYZ";

    @Autowired
    protected MockMvc mvc;

    protected ResultActions chamar(HttpMethod metodo, String caminho, String token, String corpo) throws Exception {
        MockHttpServletRequestBuilder requisicao = request(metodo, caminho)
                .header("Idempotency-Key", UUID.randomUUID().toString());
        if (token != null) {
            requisicao.header("Authorization", "Bearer " + token);
        }
        if (corpo != null) {
            requisicao.contentType(MediaType.APPLICATION_JSON).content(corpo);
        }
        return mvc.perform(requisicao);
    }

    protected ResultActions get(String caminho, String token) throws Exception {
        return chamar(HttpMethod.GET, caminho, token, null);
    }

    protected ResultActions post(String caminho, String token, String corpo) throws Exception {
        return chamar(HttpMethod.POST, caminho, token, corpo);
    }

    protected ResultActions put(String caminho, String token, String corpo) throws Exception {
        return chamar(HttpMethod.PUT, caminho, token, corpo);
    }

    protected ResultActions delete(String caminho, String token) throws Exception {
        return chamar(HttpMethod.DELETE, caminho, token, null);
    }

    protected static <T> T ler(ResultActions resultado, String caminho) throws Exception {
        return JsonPath.read(resultado.andReturn().getResponse().getContentAsString(), caminho);
    }

    protected String login(String email, String senha) throws Exception {
        ResultActions resposta = post("/auth/login", null, """
                {"email": "%s", "senha": "%s"}""".formatted(email, senha)).andExpect(status().isOk());
        return ler(resposta, "$.accessToken");
    }

    protected String tokenAdmin() throws Exception {
        return login("admin@oficina.com", "admin12345");
    }

    protected String novoMecanico(String tokenAdmin) throws Exception {
        String email = "mecanico." + unico() + "@oficina.com";
        post("/usuarios", tokenAdmin, """
                {"nome": "Marcos Mecânico", "email": "%s", "telefone": "+5511999990002",
                 "senha": "senhaMecanico1", "perfil": "MECANICO"}""".formatted(email))
                .andExpect(status().isCreated());
        return login(email, "senhaMecanico1");
    }

    protected ClienteCriado novoCliente() throws Exception {
        String email = "cliente." + unico() + "@email.com";
        ResultActions resposta = post("/clientes", null, """
                {"documento": "%s", "nome": "João da Silva", "email": "%s", "telefone": "+5511988887777",
                 "senha": "senhaCliente1"}""".formatted(cpf(), email)).andExpect(status().isCreated());
        Integer id = ler(resposta, "$.id");
        return new ClienteCriado(id, email, login(email, "senhaCliente1"));
    }

    protected Integer novoVeiculo(Integer clienteId, String token) throws Exception {
        ResultActions resposta = post("/veiculos", token, """
                {"clienteId": %d, "placa": "%s", "marca": "Volkswagen", "modelo": "Gol 1.6", "ano": 2019,
                 "cor": "Prata", "quilometragem": 85000}""".formatted(clienteId, placa()))
                .andExpect(status().isCreated());
        return ler(resposta, "$.id");
    }

    protected Integer novoServico(String tokenAdmin, String precoBase) throws Exception {
        ResultActions resposta = post("/servicos", tokenAdmin, """
                {"nome": "Troca de óleo %s", "descricao": "Mão de obra", "precoBase": "%s"}"""
                .formatted(unico(), precoBase)).andExpect(status().isCreated());
        return ler(resposta, "$.id");
    }

    protected Integer novaPeca(String token, String unidade, int saldo) throws Exception {
        ResultActions resposta = post("/pecas", token, """
                {"sku": "SKU-%s", "nome": "Filtro de óleo", "unidadeMedida": "%s", "precoCusto": "20.00",
                 "precoVenda": "35.50", "estoqueMinimo": 2}""".formatted(unico(), unidade))
                .andExpect(status().isCreated());
        Integer id = ler(resposta, "$.id");
        if (saldo > 0) {
            post("/pecas/" + id + "/movimentacoes", token, """
                    {"tipo": "ENTRADA", "quantidade": %d}""".formatted(saldo)).andExpect(status().isOk());
        }
        return id;
    }

    protected static String unico() {
        return System.nanoTime() + "" + SEQUENCIA.incrementAndGet();
    }

    protected static String cpf() {
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        int[] n = new int[11];
        for (int i = 0; i < 9; i++) {
            n[i] = aleatorio.nextInt(10);
        }
        n[9] = digito(n, 9);
        n[10] = digito(n, 10);
        StringBuilder texto = new StringBuilder();
        for (int d : n) {
            texto.append(d);
        }
        return texto.toString();
    }

    protected static String placa() {
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        return "" + letra(aleatorio) + letra(aleatorio) + letra(aleatorio) + aleatorio.nextInt(10)
                + letra(aleatorio) + aleatorio.nextInt(10) + aleatorio.nextInt(10);
    }

    private static char letra(ThreadLocalRandom aleatorio) {
        return LETRAS.charAt(aleatorio.nextInt(LETRAS.length()));
    }

    private static int digito(int[] n, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += n[i] * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    protected record ClienteCriado(Integer id, String email, String token) {
    }
}
