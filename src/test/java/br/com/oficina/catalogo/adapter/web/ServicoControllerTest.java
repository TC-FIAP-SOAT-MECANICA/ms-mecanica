package br.com.oficina.catalogo.adapter.web;

import br.com.oficina.catalogo.application.dto.CadastrarServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.usecase.CadastrarServicoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testa só a camada web (sem banco): a rota e a validação vêm da interface gerada pelo contrato,
 * e o caso de uso é substituído por um mock.
 */
@WebMvcTest(ServicoController.class)
class ServicoControllerTest {

    private static final String CORPO_VALIDO = """
            {"nome": "Troca de óleo", "preco": "149.90", "tempoEstimadoMinutos": 45}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CadastrarServicoUseCase cadastrarServico;

    @Test
    void cadastraServicoERetorna201ComLocation() throws Exception {
        var id = UUID.randomUUID();
        given(cadastrarServico.execute(any(CadastrarServicoInput.class)))
                .willReturn(new ServicoOutput(id, "Troca de óleo", null, "149.90", 45, true));

        mvc.perform(post("/servicos")
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/servicos/" + id))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.preco").value("149.90"));
    }

    @Test
    void rejeitaPrecoForaDoFormatoDoContrato() throws Exception {
        mvc.perform(post("/servicos")
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Troca de óleo", "preco": "149,9", "tempoEstimadoMinutos": 45}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void operacaoAindaNaoImplementadaResponde501() throws Exception {
        mvc.perform(put("/servicos/" + UUID.randomUUID())
                        .header("If-Match", "W/\"0\"")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isNotImplemented());
    }
}
