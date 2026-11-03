package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.cadastro.ObterClienteUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ObterClienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObterClienteConfig {

    @Bean
    public ObterClienteUseCase obterClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway) {
        return new ObterClienteUseCaseImpl(repository, usuarioGateway);
    }
}
