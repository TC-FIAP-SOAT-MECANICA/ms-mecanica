package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoInput;
import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.AtualizarPecaInsumoUseCase;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.estoque.PecaInsumo;

public class AtualizarPecaInsumoUseCaseImpl implements AtualizarPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public AtualizarPecaInsumoUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PecaInsumoOutput execute(Integer pecaId, PecaInsumoInput input) {
        PecaInsumo peca = repository.findById(pecaId)
                .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
        peca.atualizarCadastro(input.toDados());
        if (repository.existsBySku(peca.sku(), pecaId)) {
            throw new ConflictException("SKU_JA_CADASTRADO", "Já existe peça/insumo com este SKU");
        }
        return PecaInsumoOutput.from(repository.save(peca));
    }
}
