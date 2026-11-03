package br.com.oficina.infrastructure.dataproviders.database.entities.atendimento;

import br.com.oficina.domain.model.atendimento.OrdemServicoServico;
import br.com.oficina.domain.valueobject.Dinheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_ordem_servico_servicos")
public class OrdemServicoServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "servico_id", nullable = false)
    private Integer servicoId;

    @Column(name = "preco_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoFinal;

    protected OrdemServicoServicoEntity() {
    }

    public static OrdemServicoServicoEntity from(OrdemServicoServico item) {
        var entity = new OrdemServicoServicoEntity();
        entity.id = item.id();
        entity.servicoId = item.servicoId();
        entity.precoFinal = item.precoFinal().valor();
        return entity;
    }

    public OrdemServicoServico toDomain() {
        return new OrdemServicoServico(id, servicoId, new Dinheiro(precoFinal));
    }
}
