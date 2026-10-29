package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.ClienteDoUsuarioGateway;
import br.com.oficina.application.gateways.identidade.PasswordHasher;
import br.com.oficina.application.gateways.identidade.TokenIssuer;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.AutenticarUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.identidade.AutenticarUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutenticarUsuarioConfig {

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioRepository repository,
                                                             PasswordHasher passwordHasher, TokenIssuer tokenIssuer,
                                                             ClienteDoUsuarioGateway clienteDoUsuario) {
        return new AutenticarUsuarioUseCaseImpl(repository, passwordHasher, tokenIssuer, clienteDoUsuario);
    }
}
