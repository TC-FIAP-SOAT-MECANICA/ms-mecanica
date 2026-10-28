package br.com.oficina.estoque.application.usecase;

import br.com.oficina.estoque.application.dto.PecaInsumoOutput;
import br.com.oficina.estoque.application.port.PecaInsumoRepository;
import br.com.oficina.estoque.domain.PecaInsumo;
import br.com.oficina.estoque.domain.TipoMovimentacao;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import br.com.oficina.shared.domain.valueobject.Quantidade;

import java.math.BigDecimal;

public class MovimentarEstoqueUseCase {

    private final PecaInsumoRepository repository;
    private final UnitOfWork unitOfWork;

    public MovimentarEstoqueUseCase(PecaInsumoRepository repository, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.unitOfWork = unitOfWork;
    }

    public PecaInsumoOutput execute(Integer pecaId, TipoMovimentacao tipo, BigDecimal quantidade) {
        return unitOfWork.execute(() -> {
            PecaInsumo peca = repository.findByIdForUpdate(pecaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Peça/insumo", pecaId));
            switch (tipo) {
                case ENTRADA -> peca.darEntrada(Quantidade.de(quantidade));
                case SAIDA -> peca.darSaida(Quantidade.de(quantidade));
                case AJUSTE_INVENTARIO -> peca.ajustarInventario(quantidade);
            }
            return PecaInsumoOutput.from(repository.save(peca));
        });
    }
}
