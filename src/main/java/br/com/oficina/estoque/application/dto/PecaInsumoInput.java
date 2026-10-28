package br.com.oficina.estoque.application.dto;

import br.com.oficina.estoque.domain.DadosPecaInsumo;
import br.com.oficina.shared.domain.valueobject.Dinheiro;

import java.math.BigDecimal;

public record PecaInsumoInput(String sku, String nome, String unidadeMedida, String precoCusto, String precoVenda,
                              BigDecimal estoqueMinimo) {

    public DadosPecaInsumo toDados() {
        return new DadosPecaInsumo(sku, nome, unidadeMedida, Dinheiro.de(precoCusto), Dinheiro.de(precoVenda),
                estoqueMinimo);
    }
}
