package br.com.oficina.estoque.config;

import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.estoque.application.usecase.AtualizarPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.CadastrarPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.ListarPecasInsumosUseCase;
import br.com.oficina.estoque.application.usecase.MovimentarEstoqueUseCase;
import br.com.oficina.estoque.application.usecase.ObterPecaInsumoUseCase;
import br.com.oficina.estoque.application.usecase.RemoverPecaInsumoUseCase;
import br.com.oficina.shared.application.port.UnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EstoqueConfig {

    @Bean
    CadastrarPecaInsumoUseCase cadastrarPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new CadastrarPecaInsumoUseCase(repository);
    }

    @Bean
    ObterPecaInsumoUseCase obterPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new ObterPecaInsumoUseCase(repository);
    }

    @Bean
    ListarPecasInsumosUseCase listarPecasInsumosUseCase(PecaInsumoRepository repository) {
        return new ListarPecasInsumosUseCase(repository);
    }

    @Bean
    AtualizarPecaInsumoUseCase atualizarPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new AtualizarPecaInsumoUseCase(repository);
    }

    @Bean
    RemoverPecaInsumoUseCase removerPecaInsumoUseCase(PecaInsumoRepository repository) {
        return new RemoverPecaInsumoUseCase(repository);
    }

    @Bean
    MovimentarEstoqueUseCase movimentarEstoqueUseCase(PecaInsumoRepository repository, UnitOfWork unitOfWork) {
        return new MovimentarEstoqueUseCase(repository, unitOfWork);
    }
}
