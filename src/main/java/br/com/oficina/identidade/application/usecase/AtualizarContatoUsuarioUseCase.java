package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.AtualizarContatoInput;
import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Usuario;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class AtualizarContatoUsuarioUseCase {

    private final UsuarioRepository repository;

    public AtualizarContatoUsuarioUseCase(UsuarioRepository repository) {
        this.repository = repository;
    }

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
