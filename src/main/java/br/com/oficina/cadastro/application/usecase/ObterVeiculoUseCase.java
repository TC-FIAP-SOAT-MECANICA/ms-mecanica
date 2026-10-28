package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.VeiculoOutput;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterVeiculoUseCase {

    private final VeiculoRepository repository;

    public ObterVeiculoUseCase(VeiculoRepository repository) {
        this.repository = repository;
    }

    public VeiculoOutput execute(Integer veiculoId, Solicitante solicitante) {
        VeiculoOutput veiculo = execute(veiculoId);
        if (!solicitante.podeVerCliente(veiculo.clienteId())) {
            throw new ResourceNotFoundException("Veículo", veiculoId);
        }
        return veiculo;
    }

    public VeiculoOutput execute(Integer veiculoId) {
        return repository.findById(veiculoId)
                .map(VeiculoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));
    }
}
