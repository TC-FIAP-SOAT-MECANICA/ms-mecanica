package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.FuncionarioRepository;
import br.com.oficina.application.usecase.cadastro.ContratarFuncionarioUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ContratarFuncionarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContratarFuncionarioConfig {

    @Bean
    public ContratarFuncionarioUseCase contratarFuncionarioUseCase(FuncionarioRepository repository) {
        return new ContratarFuncionarioUseCaseImpl(repository);
    }
}
