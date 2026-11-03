package br.com.oficina.infrastructure.dataproviders.client.atendimento;

import br.com.oficina.application.dto.cadastro.FuncionarioOutput;
import br.com.oficina.application.gateways.atendimento.FuncionarioGateway;
import br.com.oficina.application.usecase.cadastro.ObterFuncionarioPorUsuarioUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class FuncionarioGatewayImpl implements FuncionarioGateway {

    private final ObterFuncionarioPorUsuarioUseCase obterFuncionario;

    FuncionarioGatewayImpl(ObterFuncionarioPorUsuarioUseCase obterFuncionario) {
        this.obterFuncionario = obterFuncionario;
    }

    @Override
    public Optional<FuncionarioAtendente> funcionarioDoUsuario(Integer usuarioId) {
        try {
            FuncionarioOutput funcionario = obterFuncionario.execute(usuarioId);
            return Optional.of(new FuncionarioAtendente(funcionario.id(), funcionario.ativo()));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
}
