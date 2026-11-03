package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

import java.util.Objects;

public record OrdemServicoPecaInsumo(Integer id, Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario,
                       Dinheiro subtotal) {

    public OrdemServicoPecaInsumo {
        Objects.requireNonNull(pecaInsumoId, "pecaInsumoId");
        Objects.requireNonNull(quantidade, "quantidade");
        Objects.requireNonNull(precoUnitario, "precoUnitario");
        Objects.requireNonNull(subtotal, "subtotal");
    }

    public static OrdemServicoPecaInsumo novo(Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario) {
        return new OrdemServicoPecaInsumo(null, pecaInsumoId, quantidade, precoUnitario, precoUnitario.multiplicar(quantidade));
    }
}
