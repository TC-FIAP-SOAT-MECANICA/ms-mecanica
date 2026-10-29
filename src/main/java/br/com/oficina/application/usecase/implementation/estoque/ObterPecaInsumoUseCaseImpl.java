package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.ObterPecaInsumoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterPecaInsumoUseCaseImpl implements ObterPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public ObterPecaInsumoUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PecaInsumoOutput execute(Integer pecaId) {
        return repository.findById(pecaId)
                .map(PecaInsumoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
    }
}
