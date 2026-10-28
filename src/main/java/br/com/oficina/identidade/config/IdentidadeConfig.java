package br.com.oficina.identidade.config;

import br.com.oficina.identidade.application.port.ClienteDoUsuarioPort;
import br.com.oficina.identidade.application.port.PasswordHasher;
import br.com.oficina.identidade.application.port.TokenIssuer;
import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.application.usecase.AtualizarContatoUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.AutenticarUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.CadastrarUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.CriarAdminInicialUseCase;
import br.com.oficina.identidade.application.usecase.ListarUsuariosUseCase;
import br.com.oficina.identidade.application.usecase.ObterUsuarioUseCase;
import br.com.oficina.identidade.application.usecase.RemoverUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class IdentidadeConfig {

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepository repository, PasswordHasher passwordHasher,
                                                    Clock clock) {
        return new CadastrarUsuarioUseCase(repository, passwordHasher, clock);
    }

    @Bean
    ObterUsuarioUseCase obterUsuarioUseCase(UsuarioRepository repository) {
        return new ObterUsuarioUseCase(repository);
    }

    @Bean
    ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepository repository) {
        return new ListarUsuariosUseCase(repository);
    }

    @Bean
    AtualizarContatoUsuarioUseCase atualizarContatoUsuarioUseCase(UsuarioRepository repository) {
        return new AtualizarContatoUsuarioUseCase(repository);
    }

    @Bean
    RemoverUsuarioUseCase removerUsuarioUseCase(UsuarioRepository repository) {
        return new RemoverUsuarioUseCase(repository);
    }

    @Bean
    AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioRepository repository, PasswordHasher passwordHasher,
                                                      TokenIssuer tokenIssuer, ClienteDoUsuarioPort clienteDoUsuario) {
        return new AutenticarUsuarioUseCase(repository, passwordHasher, tokenIssuer, clienteDoUsuario);
    }

    @Bean
    CriarAdminInicialUseCase criarAdminInicialUseCase(UsuarioRepository repository,
                                                      CadastrarUsuarioUseCase cadastrarUsuario) {
        return new CriarAdminInicialUseCase(repository, cadastrarUsuario);
    }
}
