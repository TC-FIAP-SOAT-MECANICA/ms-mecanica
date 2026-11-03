package br.com.oficina.entrypoints.controllers.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoInput;
import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.usecase.catalogo.AtualizarServicoUseCase;
import br.com.oficina.application.usecase.catalogo.CadastrarServicoUseCase;
import br.com.oficina.application.usecase.catalogo.ListarServicosUseCase;
import br.com.oficina.application.usecase.catalogo.ObterServicoUseCase;
import br.com.oficina.application.usecase.catalogo.RemoverServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.infrastructure.dataproviders.config.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServicoController.class)
@Import(SecurityConfig.class)
class ServicoControllerTest {

    private static final String CORPO_VALIDO = """
            {"nome": "Troca de óleo", "descricao": "Inclui filtro", "precoBase": "120.00"}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CadastrarServicoUseCase cadastrarServico;

    @MockitoBean
    private ObterServicoUseCase obterServico;

    @MockitoBean
    private ListarServicosUseCase listarServicos;

    @MockitoBean
    private AtualizarServicoUseCase atualizarServico;

    @MockitoBean
    private RemoverServicoUseCase removerServico;

    private static RequestPostProcessor perfil(String perfil) {
        return jwt().jwt(token -> token.subject("1").claim("perfil", perfil))
                .authorities(new SimpleGrantedAuthority("ROLE_" + perfil));
    }

    @Test
    void adminCadastraERecebe201ComLocation() throws Exception {
        given(cadastrarServico.execute(any(ServicoInput.class)))
                .willReturn(new ServicoOutput(7, "Troca de óleo", "Inclui filtro", "120.00"));

        mvc.perform(post("/servicos").with(perfil("ADMIN"))
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/servicos/7"))
                .andExpect(jsonPath("$.precoBase").value("120.00"));
    }

    @Test
    void mecanicoNaoCadastraServico() throws Exception {
        mvc.perform(post("/servicos").with(perfil("MECANICO"))
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("ACESSO_NEGADO"));
    }

    @Test
    void semTokenRetorna401() throws Exception {
        mvc.perform(get("/servicos/7"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("NAO_AUTENTICADO"));
    }

    @Test
    void clienteConsultaCatalogo() throws Exception {
        given(obterServico.execute(7)).willReturn(new ServicoOutput(7, "Troca de óleo", null, "120.00"));

        mvc.perform(get("/servicos/7").with(perfil("CLIENTE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Troca de óleo"));
    }

    @Test
    void rejeitaPrecoForaDoFormatoDoContrato() throws Exception {
        mvc.perform(post("/servicos").with(perfil("ADMIN"))
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Troca de óleo", "precoBase": "120,5"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void servicoInexistenteRetorna404ComCodigo() throws Exception {
        given(obterServico.execute(99)).willThrow(new ResourceNotFoundException("Serviço", 99));

        mvc.perform(get("/servicos/99").with(perfil("MECANICO")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("RECURSO_NAO_ENCONTRADO"));
    }

    @Test
    void servicoEmUsoRetorna409() throws Exception {
        willThrow(new DataIntegrityViolationException("fk_oss_servico")).given(removerServico).execute(7);

        mvc.perform(delete("/servicos/7").with(perfil("ADMIN")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("REGISTRO_EM_USO"));
    }
}
