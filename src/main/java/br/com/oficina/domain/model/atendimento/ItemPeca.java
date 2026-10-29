package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

import java.util.Objects;

public record ItemPeca(Integer id, Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario,
                       Dinheiro subtotal) {

    public ItemPeca {
        Objects.requireNonNull(pecaInsumoId, "pecaInsumoId");
        Objects.requireNonNull(quantidade, "quantidade");
        Objects.requireNonNull(precoUnitario, "precoUnitario");
        Objects.requireNonNull(subtotal, "subtotal");
    }

    public static ItemPeca novo(Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario) {
        return new ItemPeca(null, pecaInsumoId, quantidade, precoUnitario, precoUnitario.multiplicar(quantidade));
    }
}
