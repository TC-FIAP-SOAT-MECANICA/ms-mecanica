package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.CadastroGateway;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.AbrirOrdemServicoUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.AbrirOrdemServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AbrirOrdemServicoConfig {

    @Bean
    public AbrirOrdemServicoUseCase abrirOrdemServicoUseCase(OrdemServicoRepository repository,
                                                             CadastroGateway cadastro, CatalogoGateway catalogo,
                                                             EstoqueGateway estoque, UnitOfWork unitOfWork,
                                                             Clock clock) {
        return new AbrirOrdemServicoUseCaseImpl(repository, cadastro, catalogo, estoque, unitOfWork, clock);
    }
}
