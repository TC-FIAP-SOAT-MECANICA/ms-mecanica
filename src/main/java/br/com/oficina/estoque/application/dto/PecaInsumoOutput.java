package br.com.oficina.estoque.application.dto;

import br.com.oficina.estoque.domain.PecaInsumo;

import java.math.BigDecimal;

public record PecaInsumoOutput(Integer id, String sku, String nome, String unidadeMedida, String precoCusto,
                               String precoVenda, BigDecimal quantidadeEstoque, BigDecimal estoqueMinimo,
                               boolean abaixoDoMinimo) {

    public static PecaInsumoOutput from(PecaInsumo peca) {
        return new PecaInsumoOutput(peca.id(), peca.sku(), peca.nome(), peca.unidadeMedida(),
                peca.precoCusto().toString(), peca.precoVenda().toString(), peca.quantidadeEstoque(),
                peca.estoqueMinimo(), peca.abaixoDoMinimo());
    }
}
