package br.com.oficina.cadastro.adapter.web;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.dto.EnderecoData;
import br.com.oficina.cadastro.application.usecase.CadastrarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import br.com.oficina.contract.api.ClientesApi;
import br.com.oficina.contract.model.ClienteRequestDto;
import br.com.oficina.contract.model.ClienteResponseDto;
import br.com.oficina.contract.model.EnderecoDTODto;
import br.com.oficina.contract.model.TipoDocumentoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Adapter de entrada de {@code /clientes}. Implementa as operações da interface gerada {@link ClientesApi};
 * as que não estão aqui (listar, pesquisar, atualizar, remover) respondem 501 pelo método default.
 */
@RestController
public class ClienteController implements ClientesApi {

    private final CadastrarClienteUseCase cadastrarCliente;
    private final ObterClienteUseCase obterCliente;

    public ClienteController(CadastrarClienteUseCase cadastrarCliente, ObterClienteUseCase obterCliente) {
        this.cadastrarCliente = cadastrarCliente;
        this.obterCliente = obterCliente;
    }

    @Override
    public ResponseEntity<ClienteResponseDto> cadastrarCliente(UUID idempotencyKey, ClienteRequestDto request) {
        // TODO: usar a Idempotency-Key para não cadastrar duas vezes em reenvios.
        var input = new CadastrarClienteInput(request.getDocumento(), request.getNome(), request.getEmail(),
                request.getTelefone(), toEnderecoData(request.getEndereco()));
        ClienteOutput cliente = cadastrarCliente.execute(input);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(cliente));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> obterCliente(UUID clienteId) {
        return ResponseEntity.ok(toResponse(obterCliente.execute(clienteId)));
    }

    // ─── Conversões entre os DTOs do contrato (HTTP) e os DTOs do caso de uso ───

    private static EnderecoData toEnderecoData(EnderecoDTODto endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoData(endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getBairro(), endereco.getCidade(), endereco.getUf(), endereco.getCep());
    }

    private static ClienteResponseDto toResponse(ClienteOutput cliente) {
        return new ClienteResponseDto()
                .id(cliente.id())
                .tipoDocumento(TipoDocumentoDto.fromValue(cliente.tipoDocumento()))
                .documento(cliente.documento())
                .nome(cliente.nome())
                .email(cliente.email())
                .telefone(cliente.telefone())
                .endereco(toEnderecoDto(cliente.endereco()))
                .criadoEm(toOffsetDateTime(cliente.criadoEm()))
                .atualizadoEm(toOffsetDateTime(cliente.atualizadoEm()));
    }

    private static EnderecoDTODto toEnderecoDto(EnderecoData endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoDTODto()
                .logradouro(endereco.logradouro())
                .numero(endereco.numero())
                .complemento(endereco.complemento())
                .bairro(endereco.bairro())
                .cidade(endereco.cidade())
                .uf(endereco.uf())
                .cep(endereco.cep());
    }

    private static OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }
}
