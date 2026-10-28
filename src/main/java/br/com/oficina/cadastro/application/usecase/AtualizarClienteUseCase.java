package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.AtualizarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.UsuarioGateway.ContatoUsuario;
import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class AtualizarClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public AtualizarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                   UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    public ClienteOutput execute(AtualizarClienteInput input, Solicitante solicitante) {
        if (!solicitante.podeVerCliente(input.clienteId())) {
            throw new ResourceNotFoundException("Cliente", input.clienteId());
        }
        return unitOfWork.execute(() -> {
            Cliente cliente = repository.findById(input.clienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", input.clienteId()));
            ContatoUsuario contato = usuarioGateway.atualizarContato(cliente.usuarioId(), input.nome(),
                    input.email(), input.telefone());
            cliente.atualizarContato(contato.nome(), contato.email(), contato.telefone());
            return ClienteOutput.from(cliente);
        });
    }
}
