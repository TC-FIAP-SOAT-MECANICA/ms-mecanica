package br.com.oficina.infrastructure.dataproviders.database.entities.atendimento;

import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordens_servico")
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "veiculo_id", nullable = false)
    private Integer veiculoId;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status;

    @Column(name = "descricao_problema", columnDefinition = "text")
    private String descricaoProblema;

    @Column(name = "diagnostico_tecnico", columnDefinition = "text")
    private String diagnosticoTecnico;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_abertura", updatable = false)
    private Instant dataAbertura;

    @Column(name = "data_conclusao")
    private Instant dataConclusao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    @OrderBy("id")
    @BatchSize(size = 50)
    private List<ItemServicoEntity> servicos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    @OrderBy("id")
    @BatchSize(size = 50)
    private List<ItemPecaEntity> pecas = new ArrayList<>();

    protected OrdemServicoEntity() {
    }

    public static OrdemServicoEntity from(OrdemServico os) {
        var entity = new OrdemServicoEntity();
        entity.id = os.id();
        entity.veiculoId = os.veiculoId();
        entity.clienteId = os.clienteId();
        entity.status = os.status();
        entity.descricaoProblema = os.descricaoProblema();
        entity.diagnosticoTecnico = os.diagnosticoTecnico();
        entity.valorTotal = os.valorTotal().valor();
        entity.dataAbertura = os.dataAbertura();
        entity.dataConclusao = os.dataConclusao();
        entity.servicos = new ArrayList<>(os.servicos().stream().map(ItemServicoEntity::from).toList());
        entity.pecas = new ArrayList<>(os.pecas().stream().map(ItemPecaEntity::from).toList());
        return entity;
    }

    public OrdemServico toDomain() {
        return OrdemServico.restaurar(id, clienteId, veiculoId, status, descricaoProblema, diagnosticoTecnico,
                dataAbertura, dataConclusao, servicos.stream().map(ItemServicoEntity::toDomain).toList(),
                pecas.stream().map(ItemPecaEntity::toDomain).toList());
    }
}
