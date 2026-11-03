package br.com.oficina.domain.model.estoque;

import br.com.oficina.domain.valueobject.Dinheiro;

import java.math.BigDecimal;

public record DadosPecaInsumo(String sku, String nome, String unidadeMedida, Dinheiro precoCusto,
                              Dinheiro precoVenda, BigDecimal estoqueMinimo) {
}
