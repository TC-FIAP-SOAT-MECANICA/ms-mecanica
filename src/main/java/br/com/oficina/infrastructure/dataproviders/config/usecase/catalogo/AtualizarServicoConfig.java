package br.com.oficina.infrastructure.dataproviders.config.usecase.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.AtualizarServicoUseCase;
import br.com.oficina.application.usecase.implementation.catalogo.AtualizarServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtualizarServicoConfig {

    @Bean
    public AtualizarServicoUseCase atualizarServicoUseCase(ServicoRepository repository) {
        return new AtualizarServicoUseCaseImpl(repository);
    }
}
