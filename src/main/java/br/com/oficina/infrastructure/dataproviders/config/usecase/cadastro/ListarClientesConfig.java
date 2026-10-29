package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.cadastro.ListarClientesUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.ListarClientesUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarClientesConfig {

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway) {
        return new ListarClientesUseCaseImpl(repository, usuarioGateway);
    }
}
