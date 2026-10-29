package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.cadastro.CadastrarClienteUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.CadastrarClienteUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastrarClienteConfig {

    @Bean
    public CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                           UnitOfWork unitOfWork) {
        return new CadastrarClienteUseCaseImpl(repository, usuarioGateway, unitOfWork);
    }
}
