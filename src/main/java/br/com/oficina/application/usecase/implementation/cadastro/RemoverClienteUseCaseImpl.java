package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.RemoverClienteUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.cadastro.Cliente;

public class RemoverClienteUseCaseImpl implements RemoverClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public RemoverClienteUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                 UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void execute(Integer clienteId) {
        unitOfWork.execute(() -> {
            Cliente cliente = repository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
            usuarioGateway.removerUsuario(cliente.usuarioId());
            return null;
        });
    }
}
