package br.com.oficina.infrastructure.dataproviders.config.usecase.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.ListarServicosUseCase;
import br.com.oficina.application.usecase.implementation.catalogo.ListarServicosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarServicosConfig {

    @Bean
    public ListarServicosUseCase listarServicosUseCase(ServicoRepository repository) {
        return new ListarServicosUseCaseImpl(repository);
    }
}
