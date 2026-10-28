package br.com.oficina.catalogo.adapter.persistence;

import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
class ServicoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    protected ServicoJpaEntity() {
    }

    static ServicoJpaEntity from(Servico servico) {
        var entity = new ServicoJpaEntity();
        entity.id = servico.id();
        entity.nome = servico.nome();
        entity.descricao = servico.descricao();
        entity.precoBase = servico.precoBase().valor();
        return entity;
    }

    Servico toDomain() {
        return Servico.restaurar(id, nome, descricao, new Dinheiro(precoBase));
    }
}
