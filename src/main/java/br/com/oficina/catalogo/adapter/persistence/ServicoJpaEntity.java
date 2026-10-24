package br.com.oficina.catalogo.adapter.persistence;

import br.com.oficina.catalogo.domain.Servico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representação da tabela {@code servico} para o JPA.
 *
 * <p>É uma classe separada da entidade de domínio {@link Servico} de propósito: as anotações de banco
 * ficam só aqui, e o domínio continua sem saber que existe JPA.
 */
@Entity
@Table(name = "servico")
class ServicoJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal preco;

    @Column(name = "tempo_estimado_minutos", nullable = false)
    private int tempoEstimadoMinutos;

    @Column(nullable = false)
    private boolean ativo;

    protected ServicoJpaEntity() {
        // exigido pelo JPA
    }

    static ServicoJpaEntity from(Servico servico) {
        var entity = new ServicoJpaEntity();
        entity.id = servico.id();
        entity.nome = servico.nome();
        entity.descricao = servico.descricao();
        entity.preco = servico.preco().valor();
        entity.tempoEstimadoMinutos = servico.tempoEstimadoMinutos();
        entity.ativo = servico.ativo();
        return entity;
    }
}
