package br.com.oficina.application.usecase.implementation.estoque;

import br.com.oficina.application.dto.estoque.PecaInsumoOutput;
import br.com.oficina.application.gateways.estoque.PecaInsumoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.application.usecase.estoque.MovimentarEstoqueUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.estoque.PecaInsumo;
import br.com.oficina.domain.model.estoque.TipoMovimentacao;
import br.com.oficina.domain.valueobject.Quantidade;

import java.math.BigDecimal;

public class MovimentarEstoqueUseCaseImpl implements MovimentarEstoqueUseCase {

    private final PecaInsumoRepository repository;
    private final UnitOfWork unitOfWork;

    public MovimentarEstoqueUseCaseImpl(PecaInsumoRepository repository, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public PecaInsumoOutput execute(Integer pecaId, String tipo, BigDecimal quantidade) {
        return unitOfWork.execute(() -> {
            PecaInsumo peca = repository.findByIdForUpdate(pecaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
            switch (TipoMovimentacao.valueOf(tipo)) {
                case ENTRADA -> peca.darEntrada(Quantidade.de(quantidade));
                case SAIDA -> peca.darSaida(Quantidade.de(quantidade));
                case AJUSTE_INVENTARIO -> peca.ajustarInventario(quantidade);
            }
            return PecaInsumoOutput.from(repository.save(peca));
        });
    }
}
