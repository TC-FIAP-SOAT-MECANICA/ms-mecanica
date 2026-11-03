package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.EntregarVeiculoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.EntregarVeiculoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntregarVeiculoConfig {

    @Bean
    public EntregarVeiculoUseCase entregarVeiculoUseCase(OrdemServicoRepository repository) {
        return new EntregarVeiculoUseCaseImpl(repository);
    }
}
