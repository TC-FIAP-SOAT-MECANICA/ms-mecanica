package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.AtualizarClienteUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.AtualizarClienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtualizarClienteConfig {

    @Bean
    public AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                           UnitOfWork unitOfWork) {
        return new AtualizarClienteUseCaseImpl(repository, usuarioGateway, unitOfWork);
    }
}
