package br.com.oficina.infrastructure.dataproviders.config.usecase.atendimento;

import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.RemoverServicoDaOrdemUseCase;
import br.com.oficina.application.usecase.implementation.atendimento.RemoverServicoDaOrdemUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverServicoDaOrdemConfig {

    @Bean
    public RemoverServicoDaOrdemUseCase removerServicoDaOrdemUseCase(OrdemServicoRepository repository) {
        return new RemoverServicoDaOrdemUseCaseImpl(repository);
    }
}
