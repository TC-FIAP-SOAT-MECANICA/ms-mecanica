package br.com.oficina.catalogo.config;

import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.application.usecase.AtualizarServicoUseCase;
import br.com.oficina.catalogo.application.usecase.CadastrarServicoUseCase;
import br.com.oficina.catalogo.application.usecase.ListarServicosUseCase;
import br.com.oficina.catalogo.application.usecase.ObterServicoUseCase;
import br.com.oficina.catalogo.application.usecase.RemoverServicoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CatalogoConfig {

    @Bean
    CadastrarServicoUseCase cadastrarServicoUseCase(ServicoRepository repository) {
        return new CadastrarServicoUseCase(repository);
    }

    @Bean
    ObterServicoUseCase obterServicoUseCase(ServicoRepository repository) {
        return new ObterServicoUseCase(repository);
    }

    @Bean
    ListarServicosUseCase listarServicosUseCase(ServicoRepository repository) {
        return new ListarServicosUseCase(repository);
    }

    @Bean
    AtualizarServicoUseCase atualizarServicoUseCase(ServicoRepository repository) {
        return new AtualizarServicoUseCase(repository);
    }

    @Bean
    RemoverServicoUseCase removerServicoUseCase(ServicoRepository repository) {
        return new RemoverServicoUseCase(repository);
    }
}
