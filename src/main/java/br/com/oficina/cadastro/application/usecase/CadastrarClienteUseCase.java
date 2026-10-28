package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.UsuarioGateway.UsuarioCriado;
import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ForbiddenException;
import br.com.oficina.shared.domain.valueobject.Documento;

public class CadastrarClienteUseCase {

    private final ClienteRepository repository;
    private final UsuarioGateway usuarioGateway;
    private final UnitOfWork unitOfWork;

    public CadastrarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                   UnitOfWork unitOfWork) {
        this.repository = repository;
        this.usuarioGateway = usuarioGateway;
        this.unitOfWork = unitOfWork;
    }

    public ClienteOutput execute(CadastrarClienteInput input, Solicitante solicitante) {
        if (solicitante.isCliente()) {
            throw new ForbiddenException("Cliente autenticado não pode cadastrar outro cliente");
        }
        Documento documento = new Documento(input.documento());
        return unitOfWork.execute(() -> {
            if (repository.existsByDocumento(documento)) {
                throw new ConflictException("DOCUMENTO_JA_CADASTRADO", "Já existe cliente com este CPF/CNPJ");
            }
            UsuarioCriado usuario = usuarioGateway.cadastrarUsuarioCliente(input.nome(), input.email(),
                    input.telefone(), input.senha());
            Cliente cliente = Cliente.cadastrar(usuario.id(), documento, usuario.nome(), usuario.email(),
                    usuario.telefone(), usuario.criadoEm());
            return ClienteOutput.from(repository.save(cliente));
        });
    }
}
