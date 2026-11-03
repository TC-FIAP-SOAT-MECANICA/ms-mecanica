package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.AtualizarContatoInput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.AtualizarContatoUsuarioUseCase;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.identidade.Usuario;

public class AtualizarContatoUsuarioUseCaseImpl implements AtualizarContatoUsuarioUseCase {

    private final UsuarioRepository repository;

    public AtualizarContatoUsuarioUseCaseImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsuarioOutput execute(AtualizarContatoInput input) {
        Usuario usuario = repository.findById(input.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", input.usuarioId()));
        if (repository.existsByEmailAndIdNot(Usuario.normalizarEmail(input.email()), usuario.id())) {
            throw new ConflictException("EMAIL_JA_CADASTRADO", "Já existe usuário com este e-mail");
        }
        usuario.atualizarContato(input.nome(), input.email(), input.telefone());
        return UsuarioOutput.from(repository.save(usuario));
    }
}
