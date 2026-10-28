package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class ObterPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public ObterPecaInsumoUseCase(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    public PecaInsumoOutput execute(Integer pecaId) {
        return repository.findById(pecaId)
                .map(PecaInsumoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
    }
}
