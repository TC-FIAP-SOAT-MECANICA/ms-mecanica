package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.ListarUsuariosUseCase;
import br.com.oficina.application.usecase.implementation.identidade.ListarUsuariosUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListarUsuariosConfig {

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepository repository) {
        return new ListarUsuariosUseCaseImpl(repository);
    }
}
