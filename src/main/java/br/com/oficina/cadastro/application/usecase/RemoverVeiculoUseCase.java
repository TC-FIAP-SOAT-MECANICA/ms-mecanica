package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverVeiculoUseCase {

    private final VeiculoRepository repository;

    public RemoverVeiculoUseCase(VeiculoRepository repository) {
        this.repository = repository;
    }

    public void execute(Integer veiculoId) {
        if (repository.findById(veiculoId).isEmpty()) {
            throw new ResourceNotFoundException("Veículo", veiculoId);
        }
        repository.deleteById(veiculoId);
    }
}
