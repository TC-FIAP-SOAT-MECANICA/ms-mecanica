package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.ObterVeiculoUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ObterVeiculoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterVeiculoConfig {

    @Bean
    public ObterVeiculoUseCase obterVeiculoUseCase(VeiculoRepository repository) {
        return new ObterVeiculoUseCaseImpl(repository);
    }
}
