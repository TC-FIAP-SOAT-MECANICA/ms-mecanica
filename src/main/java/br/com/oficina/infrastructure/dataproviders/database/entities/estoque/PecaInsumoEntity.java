package br.com.oficina.infrastructure.dataproviders.database.entities.estoque;

import br.com.oficina.domain.model.estoque.DadosPecaInsumo;
import br.com.oficina.domain.model.estoque.PecaInsumo;
import br.com.oficina.domain.valueobject.Dinheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "pecas_insumos")
public class PecaInsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "unidade_medida", nullable = false, length = 10)
    private String unidadeMedida;

    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeEstoque;

    @Column(name = "estoque_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal estoqueMinimo;

    protected PecaInsumoEntity() {
    }

    public static PecaInsumoEntity from(PecaInsumo peca) {
        var entity = new PecaInsumoEntity();
        entity.id = peca.id();
        entity.sku = peca.sku();
        entity.nome = peca.nome();
        entity.unidadeMedida = peca.unidadeMedida();
        entity.precoCusto = peca.precoCusto().valor();
        entity.precoVenda = peca.precoVenda().valor();
        entity.quantidadeEstoque = peca.quantidadeEstoque();
        entity.estoqueMinimo = peca.estoqueMinimo();
        return entity;
    }

    public PecaInsumo toDomain() {
        return PecaInsumo.restaurar(id, new DadosPecaInsumo(sku, nome, unidadeMedida, new Dinheiro(precoCusto),
                new Dinheiro(precoVenda), estoqueMinimo), quantidadeEstoque);
    }
}
