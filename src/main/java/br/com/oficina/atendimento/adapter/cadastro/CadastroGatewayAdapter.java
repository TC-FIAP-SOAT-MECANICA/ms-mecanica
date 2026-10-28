package br.com.oficina.atendimento.adapter.cadastro;

import br.com.oficina.atendimento.application.port.CadastroGateway;
import br.com.oficina.cadastro.application.usecase.ObterClienteUseCase;
import br.com.oficina.cadastro.application.usecase.ObterVeiculoUseCase;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class CadastroGatewayAdapter implements CadastroGateway {

    private final ObterClienteUseCase obterCliente;
    private final ObterVeiculoUseCase obterVeiculo;

    CadastroGatewayAdapter(ObterClienteUseCase obterCliente, ObterVeiculoUseCase obterVeiculo) {
        this.obterCliente = obterCliente;
        this.obterVeiculo = obterVeiculo;
    }

    @Override
    public boolean clienteExiste(Integer clienteId) {
        try {
            obterCliente.execute(clienteId, Solicitante.sistema());
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public Optional<Integer> clienteDoVeiculo(Integer veiculoId) {
        try {
            return Optional.of(obterVeiculo.execute(veiculoId, Solicitante.sistema()).clienteId());
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
}
