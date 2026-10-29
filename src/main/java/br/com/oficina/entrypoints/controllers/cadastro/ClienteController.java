package br.com.oficina.entrypoints.controllers.cadastro;

import br.com.oficina.application.dto.cadastro.CadastrarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.usecase.cadastro.AtualizarClienteUseCase;
import br.com.oficina.application.usecase.cadastro.CadastrarClienteUseCase;
import br.com.oficina.application.usecase.cadastro.ListarClientesUseCase;
import br.com.oficina.application.usecase.cadastro.ObterClienteUseCase;
import br.com.oficina.application.usecase.cadastro.PesquisarClientePorDocumentoUseCase;
import br.com.oficina.application.usecase.cadastro.RemoverClienteUseCase;
import br.com.oficina.contract.api.ClientesApi;
import br.com.oficina.contract.model.AtualizarClienteRequestDto;
import br.com.oficina.contract.model.ClienteRequestDto;
import br.com.oficina.contract.model.ClienteResponseDto;
import br.com.oficina.contract.model.PaginaClientesDto;
import br.com.oficina.contract.model.PesquisaClienteRequestDto;
import br.com.oficina.entrypoints.controllers.presenter.cadastro.ClientePresenter;
import br.com.oficina.infrastructure.dataproviders.config.security.SolicitanteAtual;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        CadastrarClienteInput input = ClientePresenter.toInput(request);
        ClienteOutput cliente = SolicitanteAtual.opcional()
                .map(solicitante -> cadastrarCliente.execute(input, solicitante))
                .orElseGet(() -> cadastrarCliente.execute(input));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();
        return ResponseEntity.created(location).body(ClientePresenter.toResponse(cliente));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> obterCliente(Integer clienteId) {
        return ResponseEntity.ok(ClientePresenter.toResponse(obterCliente.execute(clienteId, SolicitanteAtual.get())));
    }

    @Override
    public ResponseEntity<PaginaClientesDto> listarClientes(Integer page, Integer size, String nome) {
        PageResult<ClienteOutput> pagina = listarClientes.execute(nome, PageQuery.of(page, size));
        return ResponseEntity.ok(ClientePresenter.toPagina(pagina));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> pesquisarClientePorDocumento(PesquisaClienteRequestDto request) {
        return ResponseEntity.ok(ClientePresenter.toResponse(pesquisarCliente.execute(request.getDocumento())));
    }

    @Override
    public ResponseEntity<ClienteResponseDto> atualizarCliente(Integer clienteId,
                                                               AtualizarClienteRequestDto request) {
        ClienteOutput cliente = atualizarCliente.execute(ClientePresenter.toInput(clienteId, request),
                SolicitanteAtual.get());
        return ResponseEntity.ok(ClientePresenter.toResponse(cliente));
    }

    @Override
    public ResponseEntity<Void> removerCliente(Integer clienteId) {
        removerCliente.execute(clienteId);
        return ResponseEntity.noContent().build();
    }
}
