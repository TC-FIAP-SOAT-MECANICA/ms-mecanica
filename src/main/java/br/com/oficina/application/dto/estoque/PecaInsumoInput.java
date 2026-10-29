package br.com.oficina.application.dto.estoque;

import br.com.oficina.domain.model.estoque.DadosPecaInsumo;
import br.com.oficina.domain.valueobject.Dinheiro;

import java.math.BigDecimal;

public record PecaInsumoInput(String sku, String nome, String unidadeMedida, String precoCusto, String precoVenda,
                              BigDecimal estoqueMinimo) {

    public DadosPecaInsumo toDados() {
        return new DadosPecaInsumo(sku, nome, unidadeMedida, Dinheiro.de(precoCusto), Dinheiro.de(precoVenda),
                estoqueMinimo);
    }
}
