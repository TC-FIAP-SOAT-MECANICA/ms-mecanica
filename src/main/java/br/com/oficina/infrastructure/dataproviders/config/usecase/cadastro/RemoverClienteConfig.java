package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.RemoverClienteUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.RemoverClienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoverClienteConfig {

    @Bean
    public RemoverClienteUseCase removerClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                       UnitOfWork unitOfWork) {
        return new RemoverClienteUseCaseImpl(repository, usuarioGateway, unitOfWork);
    }
}
