package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.dto.PecaInsumoInput;
import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.estoque.domain.PecaInsumo;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

public class AtualizarPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public AtualizarPecaInsumoUseCase(PecaInsumoRepository repository) {
        this.repository = repository;
    }

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
