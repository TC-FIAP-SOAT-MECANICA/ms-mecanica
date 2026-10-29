package br.com.oficina.application.usecase.implementation.identidade;

import br.com.oficina.application.dto.identidade.TokenOutput;
import br.com.oficina.application.exceptions.UnauthorizedException;
import br.com.oficina.application.gateways.identidade.ClienteDoUsuarioGateway;
import br.com.oficina.application.gateways.identidade.PasswordHasher;
import br.com.oficina.application.gateways.identidade.TokenIssuer;
import br.com.oficina.application.gateways.identidade.TokenIssuer.TokenEmitido;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.AutenticarUsuarioUseCase;
import br.com.oficina.domain.model.identidade.Perfil;
import br.com.oficina.domain.model.identidade.Usuario;

public class AutenticarUsuarioUseCaseImpl implements AutenticarUsuarioUseCase {

    private static final String CREDENCIAIS_INVALIDAS = "E-mail ou senha inválidos";

    private final UsuarioRepository repository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;
    private final ClienteDoUsuarioGateway clienteDoUsuario;

    public AutenticarUsuarioUseCaseImpl(UsuarioRepository repository, PasswordHasher passwordHasher,
                                    TokenIssuer tokenIssuer, ClienteDoUsuarioGateway clienteDoUsuario) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
        this.clienteDoUsuario = clienteDoUsuario;
    }

    @Override
    public TokenOutput execute(String email, String senha) {
        if (email == null || senha == null) {
            throw new UnauthorizedException(CREDENCIAIS_INVALIDAS);
        }
        Usuario usuario = repository.findByEmail(Usuario.normalizarEmail(email))
                .filter(encontrado -> passwordHasher.matches(senha, encontrado.senhaHash()))
                .orElseThrow(() -> new UnauthorizedException(CREDENCIAIS_INVALIDAS));
        Integer clienteId = usuario.perfil() == Perfil.CLIENTE
                ? clienteDoUsuario.clienteIdDoUsuario(usuario.id())
                        .orElseThrow(() -> new UnauthorizedException(CREDENCIAIS_INVALIDAS))
                : null;
        TokenEmitido token = tokenIssuer.emitir(usuario, clienteId);
        return new TokenOutput(token.valor(), token.expiraEmSegundos(), usuario.id(), usuario.perfil().name(),
                clienteId);
    }
}
