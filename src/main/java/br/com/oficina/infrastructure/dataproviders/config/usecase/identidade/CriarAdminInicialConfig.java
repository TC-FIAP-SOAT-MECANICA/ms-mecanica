package br.com.oficina.infrastructure.dataproviders.config.usecase.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.application.usecase.identidade.CadastrarUsuarioUseCase;
import br.com.oficina.application.usecase.identidade.CriarAdminInicialUseCase;
import br.com.oficina.application.usecase.implementation.identidade.CriarAdminInicialUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CriarAdminInicialConfig {

    @Bean
    public CriarAdminInicialUseCase criarAdminInicialUseCase(UsuarioRepository repository,
                                                             CadastrarUsuarioUseCase cadastrarUsuario) {
        return new CriarAdminInicialUseCaseImpl(repository, cadastrarUsuario);
    }
}
