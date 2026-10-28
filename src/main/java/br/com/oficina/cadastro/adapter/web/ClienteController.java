package br.com.oficina.cadastro.adapter.web;

import br.com.oficina.cadastro.application.dto.AtualizarClienteInput;
import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.usecase.AtualizarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.CadastrarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ListarClientesUseCase;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import br.com.oficina.cadastro.application.usecase.PesquisarClientePorDocumentoUseCase;
import br.com.oficina.cadastro.application.usecase.RemoverClienteUseCase;
import br.com.oficina.contract.api.ClientesApi;
import br.com.oficina.contract.model.AtualizarClienteRequestDto;
import br.com.oficina.contract.model.ClienteRequestDto;
import br.com.oficina.contract.model.ClienteResponseDto;
import br.com.oficina.contract.model.PaginaClientesDto;
import br.com.oficina.contract.model.PesquisaClienteRequestDto;
import br.com.oficina.contract.model.TipoDocumentoDto;
import br.com.oficina.shared.adapter.web.SolicitanteAtual;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZoneOffset;
import java.util.UUID;

@RestController
public class ClienteController implements ClientesApi {

    private final CadastrarClienteUseCase cadastrarCliente;
    private final ObterClienteUseCase obterCliente;
    private final ListarClientesUseCase listarClientes;
    private final PesquisarClientePorDocumentoUseCase pesquisarCliente;
    private final AtualizarClienteUseCase atualizarCliente;
    private final RemoverClienteUseCase removerCliente;

    public ClienteController(CadastrarClienteUseCase cadastrarCliente, ObterClienteUseCase obterCliente,
                             ListarClientesUseCase listarClientes, PesquisarClientePorDocumentoUseCase pesquisarCliente,
                             AtualizarClienteUseCase atualizarCliente, RemoverClienteUseCase removerCliente) {
        this.cadastrarCliente = cadastrarCliente;
        this.obterCliente = obterCliente;
        this.listarClientes = listarClientes;
        this.pesquisarCliente = pesquisarCliente;
        this.atualizarCliente = atualizarCliente;
        this.removerCliente = removerCliente;
    }

    @Override
    public ResponseEntity<ClienteResponseDto> cadastrarCliente(UUID idempotencyKey, ClienteRequestDto request) {
        ClienteOutput cliente = cadastrarCliente.execute(new CadastrarClienteInput(request.getDocumento(),
                request.getNome(), request.getEmail(), request.getTelefone(), request.getSenha()), SolicitanteAtual.get());
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(cliente));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> obterCliente(Integer clienteId) {
        return ResponseEntity.ok(toResponse(obterCliente.execute(clienteId, SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<PaginaClientesDto> listarClientes(Integer page, Integer size, String nome) {
        PageResult<ClienteOutput> pagina = listarClientes.execute(nome, PageQuery.of(page, size));
        return ResponseEntity.ok(new PaginaClientesDto()
                .page(pagina.page())
                .size(pagina.size())
                .totalElements(pagina.totalElements())
                .totalPages(pagina.totalPages())
                .content(pagina.content().stream().map(ClienteController::toResponse).toList()));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> pesquisarClientePorDocumento(PesquisaClienteRequestDto request) {
        return ResponseEntity.ok(toResponse(pesquisarCliente.execute(request.getDocumento())));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> atualizarCliente(Integer clienteId,
                                                               AtualizarClienteRequestDto request) {
        ClienteOutput cliente = atualizarCliente.execute(new AtualizarClienteInput(clienteId, request.getNome(),
                request.getEmail(), request.getTelefone()), SolicitanteAtual.get());
        return ResponseEntity.ok(toResponse(cliente));
    }

    @Override
    public ResponseEntity<Void> removerCliente(Integer clienteId) {
        removerCliente.execute(clienteId);
        return ResponseEntity.noContent().build();
    }

    private static ClienteResponseDto toResponse(ClienteOutput cliente) {
        return new ClienteResponseDto()
                .id(cliente.id())
                .usuarioId(cliente.usuarioId())
                .tipoDocumento(TipoDocumentoDto.fromValue(cliente.tipoDocumento()))
                .documento(cliente.documento())
                .nome(cliente.nome())
                .email(cliente.email())
                .telefone(cliente.telefone())
                .criadoEm(cliente.criadoEm() == null ? null : cliente.criadoEm().atOffset(ZoneOffset.UTC));
    }
}
