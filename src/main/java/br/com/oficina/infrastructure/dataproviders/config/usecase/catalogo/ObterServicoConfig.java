package br.com.oficina.infrastructure.dataproviders.config.usecase.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.ObterServicoUseCase;
import br.com.oficina.application.usecase.implementation.catalogo.ObterServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterServicoConfig {

    @Bean
    public ObterServicoUseCase obterServicoUseCase(ServicoRepository repository) {
        return new ObterServicoUseCaseImpl(repository);
    }
}
