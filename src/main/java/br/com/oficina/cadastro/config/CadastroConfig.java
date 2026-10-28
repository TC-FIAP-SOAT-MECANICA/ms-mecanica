package br.com.oficina.cadastro.config;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.application.usecase.AtualizarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.AtualizarVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.CadastrarClienteUseCase;
import br.com.oficina.cadastro.application.usecase.CadastrarVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.ListarClientesUseCase;
import br.com.oficina.cadastro.application.usecase.ListarVeiculosUseCase;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ObterVeiculoUseCase;
import br.com.oficina.cadastro.application.usecase.PesquisarClientePorDocumentoUseCase;
import br.com.oficina.cadastro.application.usecase.RemoverClienteUseCase;
import br.com.oficina.cadastro.application.usecase.RemoverVeiculoUseCase;
import br.com.oficina.shared.application.port.UnitOfWork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class CadastroConfig {

    @Bean
    CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                    UnitOfWork unitOfWork) {
        return new CadastrarClienteUseCase(repository, usuarioGateway, unitOfWork);
    }

    @Bean
    ObterClienteUseCase obterClienteUseCase(ClienteRepository repository) {
        return new ObterClienteUseCase(repository);
    }

    @Bean
    ListarClientesUseCase listarClientesUseCase(ClienteRepository repository) {
        return new ListarClientesUseCase(repository);
    }

    @Bean
    PesquisarClientePorDocumentoUseCase pesquisarClientePorDocumentoUseCase(ClienteRepository repository) {
        return new PesquisarClientePorDocumentoUseCase(repository);
    }

    @Bean
    AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                    UnitOfWork unitOfWork) {
        return new AtualizarClienteUseCase(repository, usuarioGateway, unitOfWork);
    }

    @Bean
    RemoverClienteUseCase removerClienteUseCase(ClienteRepository repository, UsuarioGateway usuarioGateway,
                                                UnitOfWork unitOfWork) {
        return new RemoverClienteUseCase(repository, usuarioGateway, unitOfWork);
    }

    @Bean
    CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoRepository repository,
                                                    ClienteRepository clienteRepository, Clock clock) {
        return new CadastrarVeiculoUseCase(repository, clienteRepository, clock);
    }

    @Bean
    ObterVeiculoUseCase obterVeiculoUseCase(VeiculoRepository repository) {
        return new ObterVeiculoUseCase(repository);
    }

    @Bean
    ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepository repository) {
        return new ListarVeiculosUseCase(repository);
    }

    @Bean
    AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoRepository repository,
                                                    ClienteRepository clienteRepository, Clock clock) {
        return new AtualizarVeiculoUseCase(repository, clienteRepository, clock);
    }

    @Bean
    RemoverVeiculoUseCase removerVeiculoUseCase(VeiculoRepository repository) {
        return new RemoverVeiculoUseCase(repository);
    }
}
