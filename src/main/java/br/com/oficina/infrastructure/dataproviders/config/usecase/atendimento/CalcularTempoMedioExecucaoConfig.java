package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.CalcularTempoMedioExecucaoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.CalcularTempoMedioExecucaoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalcularTempoMedioExecucaoConfig {

    @Bean
    public CalcularTempoMedioExecucaoUseCase calcularTempoMedioExecucaoUseCase(OrdemServicoRepository repository) {
        return new CalcularTempoMedioExecucaoUseCaseImpl(repository);
    }
}
