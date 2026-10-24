package br.com.oficina.catalogo.config;

import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.application.usecase.CadastrarServicoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Montagem do módulo: cria o caso de uso e entrega a ele a implementação do port.
 *
 * <p>O Spring encontra o {@code ServicoRepositoryAdapter} (anotado com {@code @Component}) e o injeta aqui
 * como {@link ServicoRepository}. É o único lugar onde caso de uso e Spring se encontram.
 */
@Configuration
class CatalogoConfig {

    @Bean
    CadastrarServicoUseCase cadastrarServicoUseCase(ServicoRepository repository) {
        return new CadastrarServicoUseCase(repository);
    }
}
