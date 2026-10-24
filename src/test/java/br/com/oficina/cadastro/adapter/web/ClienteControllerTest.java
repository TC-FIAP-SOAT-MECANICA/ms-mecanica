package br.com.oficina.cadastro.adapter.web;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.usecase.CadastrarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Testa só a camada web de {@code /clientes}: os casos de uso são mocks. */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    private static final String CORPO_VALIDO = """
            {"documento": "52998224725", "nome": "João da Silva", "email": "joao@email.com",
             "endereco": {"cidade": "São Paulo", "uf": "SP"}}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CadastrarClienteUseCase cadastrarCliente;

    @MockitoBean
    private ObterClienteUseCase obterCliente;

    @Test
    void cadastraClienteERetorna201() throws Exception {
        var cliente = clienteOutput(UUID.randomUUID());
        given(cadastrarCliente.execute(any(CadastrarClienteInput.class))).willReturn(cliente);

        mvc.perform(post("/clientes")
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/clientes/" + cliente.id()))
                .andExpect(jsonPath("$.tipoDocumento").value("CPF"))
                .andExpect(jsonPath("$.criadoEm").exists());
    }

    @Test
    void documentoDuplicadoRetorna409() throws Exception {
        given(cadastrarCliente.execute(any(CadastrarClienteInput.class)))
                .willThrow(new ConflictException("Já existe cliente com este CPF/CNPJ"));

        mvc.perform(post("/clientes")
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPO_VALIDO))
                .andExpect(status().isConflict());
    }

    @Test
    void obtemClientePorId() throws Exception {
        var id = UUID.randomUUID();
        given(obterCliente.execute(id)).willReturn(clienteOutput(id));

        mvc.perform(get("/clientes/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.endereco.uf").value("SP"));
    }

    @Test
    void clienteInexistenteRetorna404() throws Exception {
        var id = UUID.randomUUID();
        given(obterCliente.execute(id)).willThrow(new ResourceNotFoundException("Cliente", id));

        mvc.perform(get("/clientes/" + id)).andExpect(status().isNotFound());
    }

    private static ClienteOutput clienteOutput(UUID id) {
        var agora = Instant.parse("2026-09-29T12:00:00Z");
        return new ClienteOutput(id, "CPF", "52998224725", "João da Silva", "joao@email.com", null,
                new br.com.oficina.cadastro.application.dto.EnderecoData(null, null, null, null, "São Paulo", "SP",
                        null),
                agora, agora);
    }
}
