package br.com.oficina.cadastro.config;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.usecase.CadastrarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/** Montagem do módulo: cria os casos de uso e entrega a eles a implementação do port e o relógio. */
@Configuration
class CadastroConfig {

    @Bean
    CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepository repository, Clock clock) {
        return new CadastrarClienteUseCase(repository, clock);
    }

    @Bean
    ObterClienteUseCase obterClienteUseCase(ClienteRepository repository) {
        return new ObterClienteUseCase(repository);
    }
}
