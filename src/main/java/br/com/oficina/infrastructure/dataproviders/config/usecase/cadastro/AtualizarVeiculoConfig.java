package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.AtualizarVeiculoUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.AtualizarVeiculoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AtualizarVeiculoConfig {

    @Bean
    public AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoRepository repository,
                                                           ClienteRepository clienteRepository, Clock clock) {
        return new AtualizarVeiculoUseCaseImpl(repository, clienteRepository, clock);
    }
}
