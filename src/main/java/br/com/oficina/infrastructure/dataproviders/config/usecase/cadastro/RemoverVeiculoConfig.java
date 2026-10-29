package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.RemoverVeiculoUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.RemoverVeiculoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverVeiculoConfig {

    @Bean
    public RemoverVeiculoUseCase removerVeiculoUseCase(VeiculoRepository repository) {
        return new RemoverVeiculoUseCaseImpl(repository);
    }
}
