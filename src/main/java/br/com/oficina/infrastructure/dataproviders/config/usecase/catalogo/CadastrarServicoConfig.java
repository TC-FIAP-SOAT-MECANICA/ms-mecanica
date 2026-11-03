package br.com.oficina.infrastructure.dataproviders.config.usecase.catalogo;

import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.CadastrarServicoUseCase;
import br.com.oficina.application.usecase.implementation.catalogo.CadastrarServicoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadastrarServicoConfig {

    @Bean
    public CadastrarServicoUseCase cadastrarServicoUseCase(ServicoRepository repository) {
        return new CadastrarServicoUseCaseImpl(repository);
    }
}
