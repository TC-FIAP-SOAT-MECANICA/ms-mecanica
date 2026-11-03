package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.FuncionarioGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.IniciarDiagnosticoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.IniciarDiagnosticoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IniciarDiagnosticoConfig {

    @Bean
    public IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase(OrdemServicoRepository repository,
                                                               FuncionarioGateway funcionarios) {
        return new IniciarDiagnosticoUseCaseImpl(repository, funcionarios);
    }
}
