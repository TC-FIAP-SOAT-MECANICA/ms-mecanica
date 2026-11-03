package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.CotarPecaInsumoUseCase;
import br.com.oficina.application.usecase.implementation.estoque.CotarPecaInsumoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CotarPecaInsumoConfig {

    @Bean
    public CotarPecaInsumoUseCase cotarPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new CotarPecaInsumoUseCaseImpl(repository);
    }
}
