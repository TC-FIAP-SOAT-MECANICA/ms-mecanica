package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.atendimento.RemoverPecaDaOrdemUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.RemoverPecaDaOrdemUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverPecaDaOrdemConfig {

    @Bean
    public RemoverPecaDaOrdemUseCase removerPecaDaOrdemUseCase(OrdemServicoRepository repository,
                                                               EstoqueGateway estoque, UnitOfWork unitOfWork) {
        return new RemoverPecaDaOrdemUseCaseImpl(repository, estoque, unitOfWork);
    }
}
