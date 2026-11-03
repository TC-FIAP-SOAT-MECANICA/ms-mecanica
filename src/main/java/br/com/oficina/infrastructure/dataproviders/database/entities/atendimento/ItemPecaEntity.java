package br.com.oficina.infrastructure.dataproviders.database.entities.atendimento;

import br.com.oficina.domain.model.atendimento.ItemPeca;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ordem_servico_pecas_insumos")
public class ItemPecaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "peca_insumo_id", nullable = false)
    private Integer pecaInsumoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    protected ItemPecaEntity() {
    }

    public static ItemPecaEntity from(ItemPeca item) {
        var entity = new ItemPecaEntity();
        entity.id = item.id();
        entity.pecaInsumoId = item.pecaInsumoId();
        entity.quantidade = item.quantidade().valor();
        entity.precoUnitario = item.precoUnitario().valor();
        entity.subtotal = item.subtotal().valor();
        return entity;
    }

    public ItemPeca toDomain() {
        return new ItemPeca(id, pecaInsumoId, new Quantidade(quantidade), new Dinheiro(precoUnitario),
                new Dinheiro(subtotal));
    }
}
