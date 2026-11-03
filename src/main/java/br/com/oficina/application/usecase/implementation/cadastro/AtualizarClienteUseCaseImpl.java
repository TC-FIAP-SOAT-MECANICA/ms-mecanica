package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.AtualizarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway.ContatoUsuario;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.AtualizarClienteUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.cadastro.Cliente;

public class AtualizarClienteUseCaseImpl implements AtualizarClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public AtualizarClienteUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                   UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public ClienteOutput execute(AtualizarClienteInput input, Solicitante solicitante) {
        if (!solicitante.podeVerCliente(input.clienteId())) {
            throw new ResourceNotFoundException("Cliente", input.clienteId());
        }
        return unitOfWork.execute(() -> {
            Cliente cliente = repository.findById(input.clienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", input.clienteId()));
            ContatoUsuario contato = usuarioGateway.atualizarContato(cliente.usuarioId(), input.nome(),
                    input.email(), input.telefone());
            return ClienteOutput.from(cliente, contato);
        });
    }
}
