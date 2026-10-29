package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.RemoverVeiculoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class RemoverVeiculoUseCaseImpl implements RemoverVeiculoUseCase {

    private final VeiculoRepository repository;

    public RemoverVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Integer veiculoId) {
        if (repository.findById(veiculoId).isEmpty()) {
            throw new ResourceNotFoundException("Veículo", veiculoId);
        }
        repository.deleteById(veiculoId);
    }
}
