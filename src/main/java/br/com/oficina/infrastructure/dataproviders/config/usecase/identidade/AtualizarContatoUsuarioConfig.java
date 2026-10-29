package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.AtualizarContatoUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.identidade.AtualizarContatoUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtualizarContatoUsuarioConfig {

    @Bean
    public AtualizarContatoUsuarioUseCase atualizarContatoUsuarioUseCase(UsuarioRepository repository) {
        return new AtualizarContatoUsuarioUseCaseImpl(repository);
    }
}
