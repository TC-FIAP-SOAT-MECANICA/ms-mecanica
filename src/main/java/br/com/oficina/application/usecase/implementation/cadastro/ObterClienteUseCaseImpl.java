package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.cadastro.ObterClienteUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterClienteUseCaseImpl implements ObterClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;

    public ObterClienteUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public ClienteOutput execute(Integer clienteId, Solicitante solicitante) {
        if (!solicitante.podeVerCliente(clienteId)) {
            throw new ResourceNotFoundException("Cliente", clienteId);
        }
        return execute(clienteId);
    }

    @Override
    public ClienteOutput execute(Integer clienteId) {
        return repository.findById(clienteId)
                .map(cliente -> ClienteOutput.from(cliente, usuarioGateway.buscarContato(cliente.usuarioId())))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
    }
}
