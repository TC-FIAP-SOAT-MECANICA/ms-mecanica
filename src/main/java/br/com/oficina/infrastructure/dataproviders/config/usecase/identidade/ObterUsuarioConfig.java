package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.ObterUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.identidade.ObterUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterUsuarioConfig {

    @Bean
    public ObterUsuarioUseCase obterUsuarioUseCase(UsuarioRepository repository) {
        return new ObterUsuarioUseCaseImpl(repository);
    }
}
