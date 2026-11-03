package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.RemoverPecaInsumoUseCase;
import br.com.oficina.application.usecase.implementation.estoque.RemoverPecaInsumoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverPecaInsumoConfig {

    @Bean
    public RemoverPecaInsumoUseCase removerPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new RemoverPecaInsumoUseCaseImpl(repository);
    }
}
