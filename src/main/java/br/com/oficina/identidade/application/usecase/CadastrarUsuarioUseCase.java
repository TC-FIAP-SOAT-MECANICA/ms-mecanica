package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.CadastrarUsuarioInput;
import br.com.oficina.identidade.application.dto.UsuarioOutput;
import br.com.oficina.identidade.application.port.PasswordHasher;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Perfil;
import br.com.oficina.identidade.domain.Usuario;
import br.com.oficina.shared.domain.exception.ConflictException;

import java.time.Clock;

public class CadastrarUsuarioUseCase {

    private static final int SENHA_MINIMA = 8;

    private final UsuarioRepository repository;
    private final PasswordHasher passwordHasher;
    private final Clock clock;

    public CadastrarUsuarioUseCase(UsuarioRepository repository, PasswordHasher passwordHasher, Clock clock) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
    }

    public UsuarioOutput execute(CadastrarUsuarioInput input) {
        if (input.senha() == null || input.senha().length() < SENHA_MINIMA) {
            throw new IllegalArgumentException("Senha deve ter ao menos " + SENHA_MINIMA + " caracteres");
        }
        if (repository.existsByEmail(Usuario.normalizarEmail(input.email()))) {
            throw new ConflictException("EMAIL_JA_CADASTRADO", "Já existe usuário com este e-mail");
        }
        Usuario usuario = Usuario.cadastrar(input.nome(), input.email(), input.telefone(),
                passwordHasher.hash(input.senha()), Perfil.valueOf(input.perfil()), clock.instant());
        return UsuarioOutput.from(repository.save(usuario));
    }
}
