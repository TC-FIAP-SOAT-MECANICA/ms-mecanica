package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.ListarOrdensServicoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.ListarOrdensServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarOrdensServicoConfig {

    @Bean
    public ListarOrdensServicoUseCase listarOrdensServicoUseCase(OrdemServicoRepository repository) {
        return new ListarOrdensServicoUseCaseImpl(repository);
    }
}
