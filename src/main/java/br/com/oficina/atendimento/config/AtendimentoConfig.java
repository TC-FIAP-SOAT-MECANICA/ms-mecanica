package br.com.oficina.atendimento.config;

import br.com.oficina.atendimento.application.port.CadastroGateway;
import br.com.oficina.atendimento.application.port.CatalogoGateway;
import br.com.oficina.atendimento.application.port.EstoqueGateway;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.application.usecase.AbrirOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.EntregarVeiculoUseCase;
import br.com.oficina.atendimento.application.usecase.FinalizarOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.IncluirPecaNaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.IncluirServicoNaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.ListarOrdensServicoUseCase;
import br.com.oficina.atendimento.application.usecase.ObterOrdemServicoUseCase;
import br.com.oficina.atendimento.application.usecase.RegistrarDiagnosticoUseCase;
import br.com.oficina.atendimento.application.usecase.RemoverPecaDaOrdemUseCase;
import br.com.oficina.atendimento.application.usecase.RemoverServicoDaOrdemUseCase;
import br.com.oficina.shared.application.port.UnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class AtendimentoConfig {

    @Bean
    AbrirOrdemServicoUseCase abrirOrdemServicoUseCase(OrdemServicoRepository repository, CadastroGateway cadastro,
                                                      CatalogoGateway catalogo, EstoqueGateway estoque,
                                                      UnitOfWork unitOfWork, Clock clock) {
        return new AbrirOrdemServicoUseCase(repository, cadastro, catalogo, estoque, unitOfWork, clock);
    }

    @Bean
    ObterOrdemServicoUseCase obterOrdemServicoUseCase(OrdemServicoRepository repository) {
        return new ObterOrdemServicoUseCase(repository);
    }

    @Bean
    ListarOrdensServicoUseCase listarOrdensServicoUseCase(OrdemServicoRepository repository) {
        return new ListarOrdensServicoUseCase(repository);
    }

    @Bean
    IncluirServicoNaOrdemUseCase incluirServicoNaOrdemUseCase(OrdemServicoRepository repository,
                                                              CatalogoGateway catalogo, EstoqueGateway estoque,
                                                              UnitOfWork unitOfWork) {
        return new IncluirServicoNaOrdemUseCase(repository, catalogo, estoque, unitOfWork);
    }

    @Bean
    RemoverServicoDaOrdemUseCase removerServicoDaOrdemUseCase(OrdemServicoRepository repository) {
        return new RemoverServicoDaOrdemUseCase(repository);
    }

    @Bean
    IncluirPecaNaOrdemUseCase incluirPecaNaOrdemUseCase(OrdemServicoRepository repository, CatalogoGateway catalogo,
                                                        EstoqueGateway estoque, UnitOfWork unitOfWork) {
        return new IncluirPecaNaOrdemUseCase(repository, catalogo, estoque, unitOfWork);
    }

    @Bean
    RemoverPecaDaOrdemUseCase removerPecaDaOrdemUseCase(OrdemServicoRepository repository, EstoqueGateway estoque,
                                                        UnitOfWork unitOfWork) {
        return new RemoverPecaDaOrdemUseCase(repository, estoque, unitOfWork);
    }

    @Bean
    RegistrarDiagnosticoUseCase registrarDiagnosticoUseCase(OrdemServicoRepository repository) {
        return new RegistrarDiagnosticoUseCase(repository);
    }

    @Bean
    FinalizarOrdemServicoUseCase finalizarOrdemServicoUseCase(OrdemServicoRepository repository, Clock clock) {
        return new FinalizarOrdemServicoUseCase(repository, clock);
    }

    @Bean
    EntregarVeiculoUseCase entregarVeiculoUseCase(OrdemServicoRepository repository) {
        return new EntregarVeiculoUseCase(repository);
    }
}
