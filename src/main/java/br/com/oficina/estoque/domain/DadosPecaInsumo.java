package br.com.oficina.estoque.domain;

import br.com.oficina.shared.domain.valueobject.Dinheiro;

import java.math.BigDecimal;

public record DadosPecaInsumo(String sku, String nome, String unidadeMedida, Dinheiro precoCusto,
                              Dinheiro precoVenda, BigDecimal estoqueMinimo) {
}
