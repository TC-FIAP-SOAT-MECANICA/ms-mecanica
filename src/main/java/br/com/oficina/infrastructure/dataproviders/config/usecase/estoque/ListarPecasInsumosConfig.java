package br.com.oficina.infrastructure.dataproviders.config.usecase.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.ListarPecasInsumosUseCase;
import br.com.oficina.application.usecase.implementation.estoque.ListarPecasInsumosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarPecasInsumosConfig {

    @Bean
    public ListarPecasInsumosUseCase listarPecasInsumosUseCase(PecaInsumoRepository repository) {
        return new ListarPecasInsumosUseCaseImpl(repository);
    }
}
