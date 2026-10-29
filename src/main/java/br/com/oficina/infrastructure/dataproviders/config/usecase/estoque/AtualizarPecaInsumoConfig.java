package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.AtualizarPecaInsumoUseCase;
import br.com.oficina.application.usecase.implementation.estoque.AtualizarPecaInsumoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtualizarPecaInsumoConfig {

    @Bean
    public AtualizarPecaInsumoUseCase atualizarPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new AtualizarPecaInsumoUseCaseImpl(repository);
    }
}
