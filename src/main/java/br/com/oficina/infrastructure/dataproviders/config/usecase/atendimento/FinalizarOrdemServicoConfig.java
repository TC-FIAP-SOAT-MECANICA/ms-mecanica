package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.FinalizarOrdemServicoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.FinalizarOrdemServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class FinalizarOrdemServicoConfig {

    @Bean
    public FinalizarOrdemServicoUseCase finalizarOrdemServicoUseCase(OrdemServicoRepository repository, Clock clock) {
        return new FinalizarOrdemServicoUseCaseImpl(repository, clock);
    }
}
