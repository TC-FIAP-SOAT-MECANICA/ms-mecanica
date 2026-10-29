package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.CadastrarPecaInsumoUseCase;
import br.com.oficina.application.usecase.implementation.estoque.CadastrarPecaInsumoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastrarPecaInsumoConfig {

    @Bean
    public CadastrarPecaInsumoUseCase cadastrarPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new CadastrarPecaInsumoUseCaseImpl(repository);
    }
}
