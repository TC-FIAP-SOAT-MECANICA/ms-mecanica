package br.com.oficina.infrastructure.dataproviders.database.entities.atendimento;

import br.com.oficina.domain.model.atendimento.ItemServico;
import br.com.oficina.domain.valueobject.Dinheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ordem_servico_servicos")
public class ItemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "servico_id", nullable = false)
    private Integer servicoId;

    @Column(name = "preco_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoFinal;

    protected ItemServicoEntity() {
    }

    public static ItemServicoEntity from(ItemServico item) {
        var entity = new ItemServicoEntity();
        entity.id = item.id();
        entity.servicoId = item.servicoId();
        entity.precoFinal = item.precoFinal().valor();
        return entity;
    }

    public ItemServico toDomain() {
        return new ItemServico(id, servicoId, new Dinheiro(precoFinal));
    }
}
