package br.com.oficina.identidade.application.usecase;

import br.com.oficina.identidade.application.dto.TokenOutput;
import br.com.oficina.identidade.application.port.ClienteDoUsuarioPort;
import br.com.oficina.identidade.application.port.PasswordHasher;
import br.com.oficina.identidade.application.port.TokenIssuer;
import br.com.oficina.identidade.application.port.TokenIssuer.TokenEmitido;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Perfil;
import br.com.oficina.identidade.domain.Usuario;
import br.com.oficina.shared.domain.exception.UnauthorizedException;

public class AutenticarUsuarioUseCase {

    private static final String CREDENCIAIS_INVALIDAS = "E-mail ou senha inválidos";

    private final UsuarioRepository repository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;
    private final ClienteDoUsuarioPort clienteDoUsuario;

    public AutenticarUsuarioUseCase(UsuarioRepository repository, PasswordHasher passwordHasher,
                                    TokenIssuer tokenIssuer, ClienteDoUsuarioPort clienteDoUsuario) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
        this.clienteDoUsuario = clienteDoUsuario;
    }

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
