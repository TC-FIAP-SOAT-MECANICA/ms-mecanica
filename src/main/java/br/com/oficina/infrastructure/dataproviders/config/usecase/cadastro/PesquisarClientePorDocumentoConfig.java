package br.com.oficina.infrastructure.dataproviders.config.usecase.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.UsuarioGateway;
import br.com.oficina.application.usecase.cadastro.PesquisarClientePorDocumentoUseCase;
import br.com.oficina.application.usecase.implementation.cadastro.PesquisarClientePorDocumentoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PesquisarClientePorDocumentoConfig {

    @Bean
    public PesquisarClientePorDocumentoUseCase pesquisarClientePorDocumentoUseCase(ClienteRepository repository,
                                                                                   UsuarioGateway usuarioGateway) {
        return new PesquisarClientePorDocumentoUseCaseImpl(repository, usuarioGateway);
    }
}
