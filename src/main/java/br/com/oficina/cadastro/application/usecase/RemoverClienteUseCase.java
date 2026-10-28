package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public RemoverClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                 UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    public void execute(Integer clienteId) {
        unitOfWork.execute(() -> {
            Cliente cliente = repository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
            usuarioGateway.removerUsuario(cliente.usuarioId());
            return null;
        });
    }
}
