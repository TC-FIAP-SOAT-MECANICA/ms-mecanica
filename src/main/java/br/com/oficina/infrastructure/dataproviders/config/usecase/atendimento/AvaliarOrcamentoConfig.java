package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.AvaliarOrcamentoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.AvaliarOrcamentoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AvaliarOrcamentoConfig {

    @Bean
    public AvaliarOrcamentoUseCase avaliarOrcamentoUseCase(OrdemServicoRepository repository, EstoqueGateway estoque,
                                                           UnitOfWork unitOfWork, Clock clock) {
        return new AvaliarOrcamentoUseCaseImpl(repository, estoque, unitOfWork, clock);
    }
}
