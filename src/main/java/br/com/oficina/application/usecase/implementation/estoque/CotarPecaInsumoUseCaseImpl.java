package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.usecase.estoque.CotarPecaInsumoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.estoque.PecaInsumo;
import br.com.oficina.domain.valueobject.Quantidade;

import java.math.BigDecimal;

public class CotarPecaInsumoUseCaseImpl implements CotarPecaInsumoUseCase {

    private final PecaInsumoRepository repository;

    public CotarPecaInsumoUseCaseImpl(PecaInsumoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PecaInsumoOutput execute(Integer pecaId, BigDecimal quantidade) {
        PecaInsumo peca = repository.findById(pecaId)
                .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
        peca.validarQuantidade(Quantidade.de(quantidade));
        return PecaInsumoOutput.from(peca);
    }
}
