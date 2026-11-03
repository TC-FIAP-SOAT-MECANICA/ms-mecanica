package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.FuncionarioRepository;
import br.com.oficina.application.usecase.cadastro.ObterFuncionarioPorUsuarioUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ObterFuncionarioPorUsuarioUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterFuncionarioPorUsuarioConfig {

    @Bean
    public ObterFuncionarioPorUsuarioUseCase obterFuncionarioPorUsuarioUseCase(FuncionarioRepository repository) {
        return new ObterFuncionarioPorUsuarioUseCaseImpl(repository);
    }
}
