package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.RemoverPecaInsumoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class RemoverPecaInsumoUseCaseImpl implements RemoverPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public RemoverPecaInsumoUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Integer pecaId) {
        if (repository.findById(pecaId).isEmpty()) {
            throw new ResourceNotFoundException("Peça/insumo", pecaId);
        }
        repository.deleteById(pecaId);
    }
}
