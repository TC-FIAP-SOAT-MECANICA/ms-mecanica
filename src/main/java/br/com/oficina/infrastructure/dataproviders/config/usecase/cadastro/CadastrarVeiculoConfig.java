package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.CadastrarVeiculoUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.CadastrarVeiculoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CadastrarVeiculoConfig {

    @Bean
    public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoRepository repository,
                                                           ClienteRepository clienteRepository, Clock clock) {
        return new CadastrarVeiculoUseCaseImpl(repository, clienteRepository, clock);
    }
}
