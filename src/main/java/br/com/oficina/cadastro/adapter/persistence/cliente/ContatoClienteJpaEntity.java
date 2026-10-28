package br.com.oficina.cadastro.adapter.persistence.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.time.Instant;

@Entity(name = "ContatoCliente")
@Table(name = "usuarios")
@Immutable
class ContatoClienteJpaEntity {

    @Id
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(name = "criado_em")
    private Instant criadoEm;

    protected ContatoClienteJpaEntity() {
    }

    String nome() {
        return nome;
    }

    String email() {
        return email;
    }

    String telefone() {
        return telefone;
    }

    Instant criadoEm() {
        return criadoEm;
    }
}
