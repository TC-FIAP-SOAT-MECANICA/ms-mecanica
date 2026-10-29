package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.CadastrarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.exceptions.ForbiddenException;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway.ContatoUsuario;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.CadastrarClienteUseCase;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.model.cadastro.Cliente;
import br.com.oficina.domain.valueobject.Documento;

public class CadastrarClienteUseCaseImpl implements CadastrarClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public CadastrarClienteUseCaseImpl(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                       UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public ClienteOutput execute(CadastrarClienteInput input, Solicitante solicitante) {
        if (solicitante.isCliente()) {
            throw new ForbiddenException("Cliente autenticado não pode cadastrar outro cliente");
        }
        return execute(input);
    }

    @Override
    public ClienteOutput execute(CadastrarClienteInput input) {
        Documento documento = new Documento(input.documento());
        return unitOfWork.execute(() -> {
            if (repository.existsByDocumento(documento)) {
                throw new ConflictException("DOCUMENTO_JA_CADASTRADO", "Já existe cliente com este CPF/CNPJ");
            }
            ContatoUsuario contato = usuarioGateway.cadastrarUsuarioCliente(input.nome(), input.email(),
                    input.telefone(), input.senha());
            Cliente cliente = repository.save(Cliente.cadastrar(contato.usuarioId(), documento));
            return ClienteOutput.from(cliente, contato);
        });
    }
}
