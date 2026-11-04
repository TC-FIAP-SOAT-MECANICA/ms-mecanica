package br.com.oficina.integracao;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IdentidadeIntegracaoTest extends IntegracaoTest {

    @Test
    void loginDoAdminDevolveTokenComPerfil() throws Exception {
        post("/auth/login", null, """
                {"email": "admin@oficina.com", "senha": "admin12345"}""")
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.perfil").value("ADMIN"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").isString());
    }

    @Test
    void loginComSenhaErradaResponde401() throws Exception {
        post("/auth/login", null, """
                {"email": "admin@oficina.com", "senha": "senhaErrada1"}""")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("NAO_AUTENTICADO"));
    }

    @Test
    void rotaProtegidaSemTokenResponde401() throws Exception {
        get("/usuarios/me", null)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("NAO_AUTENTICADO"));
    }

    @Test
    void tokenInvalidoResponde401() throws Exception {
        get("/usuarios/me", "token.invalido.qualquer").andExpect(status().isUnauthorized());
    }

    @Test
    void adminCadastraMecanicoQueConsultaOsPropriosDados() throws Exception {
        String admin = tokenAdmin();
        String email = "marcos." + unico() + "@oficina.com";
        ResultActions criado = post("/usuarios", admin, """
                {"nome": "Marcos Mecânico", "email": "%s", "telefone": "+5511999990002",
                 "senha": "senhaMecanico1", "perfil": "MECANICO"}""".formatted(email))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.perfil").value("MECANICO"))
                .andExpect(jsonPath("$.senha").doesNotExist());
        Integer id = ler(criado, "$.id");
        String mecanico = login(email, "senhaMecanico1");

        get("/usuarios/me", mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email));
        get("/usuarios/" + id, admin).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Marcos Mecânico"));
        get("/usuarios?perfil=MECANICO&size=100", admin)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(1)));
        get("/usuarios", admin).andExpect(status().isOk());
    }

    @Test
    void mecanicoNaoGerenciaUsuarios() throws Exception {
        String mecanico = novoMecanico(tokenAdmin());

        get("/usuarios", mecanico)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("ACESSO_NEGADO"));
    }

    @Test
    void recusaEmailDuplicadoSenhaCurtaEPerfilCliente() throws Exception {
        String admin = tokenAdmin();
        String email = "dup." + unico() + "@oficina.com";
        String corpo = """
                {"nome": "Ana", "email": "%s", "telefone": "+5511999990003", "senha": "%s", "perfil": "%s"}""";
        post("/usuarios", admin, corpo.formatted(email, "senhaForte1", "ADMIN")).andExpect(status().isCreated());

        post("/usuarios", admin, corpo.formatted(email.toUpperCase(), "senhaForte1", "ADMIN"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("EMAIL_JA_CADASTRADO"));
        post("/usuarios", admin, corpo.formatted("curta." + unico() + "@oficina.com", "curta", "ADMIN"))
                .andExpect(status().isBadRequest());
        post("/usuarios", admin, corpo.formatted("cli." + unico() + "@oficina.com", "senhaForte1", "CLIENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuarioInexistenteResponde404() throws Exception {
        get("/usuarios/999999", tokenAdmin())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RECURSO_NAO_ENCONTRADO"))
                .andExpect(jsonPath("$.traceId").isString());
    }

    @Test
    void parametroDeConsultaForaDoLimiteResponde400() throws Exception {
        get("/usuarios?size=500", tokenAdmin())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACAO"))
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("size")));
    }

    @Test
    void metricasSoParaAdmin() throws Exception {
        String metrica = "/metricas/tempo-medio-execucao?dataInicio=2026-01-01&dataFim=2026-01-31";

        get(metrica, tokenAdmin()).andExpect(status().isOk());
        get(metrica, novoCliente().token()).andExpect(status().isForbidden());
    }
}
