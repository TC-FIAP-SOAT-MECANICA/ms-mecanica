package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.PasswordHasher;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.identidade.CadastrarUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CadastrarUsuarioConfig {

    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepository repository, PasswordHasher passwordHasher,
                                                           Clock clock) {
        return new CadastrarUsuarioUseCaseImpl(repository, passwordHasher, clock);
    }
}
