package br.com.oficina.infrastructure.dataproviders.client.atendimento;

import br.com.oficina.application.gateways.atendimento.CadastroGateway;
import br.com.oficina.application.usecase.cadastro.ObterClienteUseCase;
import br.com.oficina.application.usecase.cadastro.ObterVeiculoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class CadastroGatewayImpl implements CadastroGateway {

    private final ObterClienteUseCase obterCliente;
    private final ObterVeiculoUseCase obterVeiculo;

    CadastroGatewayImpl(ObterClienteUseCase obterCliente, ObterVeiculoUseCase obterVeiculo) {
        this.obterCliente = obterCliente;
        this.obterVeiculo = obterVeiculo;
    }

    @Override
    public boolean clienteExiste(Integer clienteId) {
        try {
            obterCliente.execute(clienteId);
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public Optional<Integer> clienteDoVeiculo(Integer veiculoId) {
        try {
            return Optional.of(obterVeiculo.execute(veiculoId).clienteId());
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
}
