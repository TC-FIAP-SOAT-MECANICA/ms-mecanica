package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.ObterOrdemServicoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.ObterOrdemServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterOrdemServicoConfig {

    @Bean
    public ObterOrdemServicoUseCase obterOrdemServicoUseCase(OrdemServicoRepository repository) {
        return new ObterOrdemServicoUseCaseImpl(repository);
    }
}
