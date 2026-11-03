package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.ObterPecaInsumoUseCase;
import br.com.oficina.application.usecase.implementation.estoque.ObterPecaInsumoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterPecaInsumoConfig {

    @Bean
    public ObterPecaInsumoUseCase obterPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new ObterPecaInsumoUseCaseImpl(repository);
    }
}
