package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.ObterVeiculoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterVeiculoUseCaseImpl implements ObterVeiculoUseCase {

    private final VeiculoRepository repository;

    public ObterVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public VeiculoOutput execute(Integer veiculoId, Solicitante solicitante) {
        VeiculoOutput veiculo = execute(veiculoId);
        if (!solicitante.podeVerCliente(veiculo.clienteId())) {
            throw new ResourceNotFoundException("Veículo", veiculoId);
        }
        return veiculo;
    }

    @Override
    public VeiculoOutput execute(Integer veiculoId) {
        return repository.findById(veiculoId)
                .map(VeiculoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));
    }
}
