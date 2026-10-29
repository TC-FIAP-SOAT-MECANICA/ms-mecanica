package br.com.oficina.infrastructure.dataproviders.config.usecase.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.RemoverServicoUseCase;
import br.com.oficina.application.usecase.implementation.catalogo.RemoverServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverServicoConfig {

    @Bean
    public RemoverServicoUseCase removerServicoUseCase(ServicoRepository repository) {
        return new RemoverServicoUseCaseImpl(repository);
    }
}
