package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.CadastrarUsuarioInput;
import br.com.oficina.application.dto.identidade.UsuarioOutput;
import br.com.oficina.application.gateways.identidade.PasswordHasher;
import br.com.oficina.application.gateways.identidade.UsuarioEventPublisher;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.domain.model.identidade.Perfil;
import br.com.oficina.domain.model.identidade.Usuario;
import br.com.oficina.domain.model.identidade.UsuarioCadastrado;

import java.time.Clock;

public class CadastrarUsuarioUseCaseImpl implements CadastrarUsuarioUseCase {

    private static final int SENHA_MINIMA = 8;

    private final UsuarioRepository repository;
    private final UsuarioEventPublisher eventos;
    private final PasswordHasher passwordHasher;
    private final UnitOfWork unitOfWork;
    private final Clock clock;

    public CadastrarUsuarioUseCaseImpl(UsuarioRepository repository, UsuarioEventPublisher eventos,
                                       PasswordHasher passwordHasher, UnitOfWork unitOfWork, Clock clock) {
        this.repository = repository;
        this.eventos = eventos;
        this.passwordHasher = passwordHasher;
        this.unitOfWork = unitOfWork;
        this.clock = clock;
    }

    @Override
    public UsuarioOutput execute(CadastrarUsuarioInput input) {
        if (input.senha() == null || input.senha().length() < SENHA_MINIMA) {
            throw new ValidationException("Senha deve ter ao menos " + SENHA_MINIMA + " caracteres");
        }
        if (repository.existsByEmail(Usuario.normalizarEmail(input.email()))) {
            throw new ConflictException("EMAIL_JA_CADASTRADO", "Já existe usuário com este e-mail");
        }
        Perfil perfil = Perfil.valueOf(input.perfil());
        Usuario usuario = Usuario.cadastrar(input.nome(), input.email(), input.telefone(),
                passwordHasher.hash(input.senha()), perfil, clock.instant());
        return unitOfWork.execute(() -> {
            Usuario salvo = repository.save(usuario);
            eventos.publicar(UsuarioCadastrado.de(salvo));
            return UsuarioOutput.from(salvo);
        });
    }
}
