package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.IncluirPecaNaOrdemUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.IncluirPecaNaOrdemUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IncluirPecaNaOrdemConfig {

    @Bean
    public IncluirPecaNaOrdemUseCase incluirPecaNaOrdemUseCase(OrdemServicoRepository repository,
                                                               CatalogoGateway catalogo, EstoqueGateway estoque,
                                                               UnitOfWork unitOfWork) {
        return new IncluirPecaNaOrdemUseCaseImpl(repository, catalogo, estoque, unitOfWork);
    }
}
