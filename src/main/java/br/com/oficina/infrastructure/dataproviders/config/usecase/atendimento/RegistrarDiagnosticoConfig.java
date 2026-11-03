package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.RegistrarDiagnosticoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.RegistrarDiagnosticoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrarDiagnosticoConfig {

    @Bean
    public RegistrarDiagnosticoUseCase registrarDiagnosticoUseCase(OrdemServicoRepository repository,
                                                                   EstoqueGateway estoque, UnitOfWork unitOfWork) {
        return new RegistrarDiagnosticoUseCaseImpl(repository, estoque, unitOfWork);
    }
}
