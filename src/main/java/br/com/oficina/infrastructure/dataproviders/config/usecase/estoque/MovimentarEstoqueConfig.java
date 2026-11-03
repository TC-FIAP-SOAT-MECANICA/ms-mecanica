package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.estoque.MovimentarEstoqueUseCase;
import br.com.oficina.application.usecase.implementation.estoque.MovimentarEstoqueUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovimentarEstoqueConfig {

    @Bean
    public MovimentarEstoqueUseCase movimentarEstoqueUseCase(PecaInsumoRepository repository, UnitOfWork unitOfWork) {
        return new MovimentarEstoqueUseCaseImpl(repository, unitOfWork);
    }
}
