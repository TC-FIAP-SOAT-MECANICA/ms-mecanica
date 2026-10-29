package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.RemoverUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.identidade.RemoverUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverUsuarioConfig {

    @Bean
    public RemoverUsuarioUseCase removerUsuarioUseCase(UsuarioRepository repository) {
        return new RemoverUsuarioUseCaseImpl(repository);
    }
}
