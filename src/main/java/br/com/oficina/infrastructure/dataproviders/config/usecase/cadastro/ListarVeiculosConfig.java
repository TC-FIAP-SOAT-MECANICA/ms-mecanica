package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.ListarVeiculosUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ListarVeiculosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarVeiculosConfig {

    @Bean
    public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepository repository) {
        return new ListarVeiculosUseCaseImpl(repository);
    }
}
