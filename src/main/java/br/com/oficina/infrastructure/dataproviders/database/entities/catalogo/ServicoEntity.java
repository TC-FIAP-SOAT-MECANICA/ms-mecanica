package br.com.oficina.infrastructure.dataproviders.database.entities.catalogo;

import br.com.oficina.domain.model.catalogo.Servico;
import br.com.oficina.domain.valueobject.Dinheiro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    protected ServicoEntity() {
    }

    public static ServicoEntity from(Servico servico) {
        var entity = new ServicoEntity();
        entity.id = servico.id();
        entity.nome = servico.nome();
        entity.descricao = servico.descricao();
        entity.precoBase = servico.precoBase().valor();
        return entity;
    }

    public Servico toDomain() {
        return Servico.restaurar(id, nome, descricao, new Dinheiro(precoBase));
    }
}
