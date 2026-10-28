package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class RemoverPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public RemoverPecaInsumoUseCase(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    public void execute(Integer pecaId) {
        if (repository.findById(pecaId).isEmpty()) {
            throw new ResourceNotFoundException("Peça/insumo", pecaId);
        }
        repository.deleteById(pecaId);
    }
}
