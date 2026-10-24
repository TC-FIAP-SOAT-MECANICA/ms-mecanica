package br.com.oficina.cadastro.adapter.persistence;

import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.cadastro.domain.Endereco;
import br.com.oficina.shared.domain.valueobject.Documento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Representação da tabela {@code cliente} para o JPA — separada da entidade de domínio {@link Cliente}.
 *
 * <p>Faz a conversão nos dois sentidos: {@link #from(Cliente)} para gravar e {@link #toDomain()} para ler.
 * Value Objects viram colunas simples: {@code Documento} vira uma string e o {@code Endereco} é
 * "achatado" em colunas da própria tabela.
 */
@Entity
@Table(name = "cliente")
class ClienteJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 14)
    private String documento;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(length = 16)
    private String telefone;

    @Column(name = "endereco_logradouro", length = 150)
    private String logradouro;

    @Column(name = "endereco_numero", length = 10)
    private String numero;

    @Column(name = "endereco_complemento", length = 60)
    private String complemento;

    @Column(name = "endereco_bairro", length = 80)
    private String bairro;

    @Column(name = "endereco_cidade", length = 80)
    private String cidade;

    @Column(name = "endereco_uf", length = 2)
    private String uf;

    @Column(name = "endereco_cep", length = 8)
    private String cep;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected ClienteJpaEntity() {
        // exigido pelo JPA
    }

    static ClienteJpaEntity from(Cliente cliente) {
        var entity = new ClienteJpaEntity();
        entity.id = cliente.id();
        entity.documento = cliente.documento().valor();
        entity.nome = cliente.nome();
        entity.email = cliente.email();
        entity.telefone = cliente.telefone();
        Endereco endereco = cliente.endereco();
        if (endereco != null) {
            entity.logradouro = endereco.logradouro();
            entity.numero = endereco.numero();
            entity.complemento = endereco.complemento();
            entity.bairro = endereco.bairro();
            entity.cidade = endereco.cidade();
            entity.uf = endereco.uf();
            entity.cep = endereco.cep();
        }
        entity.criadoEm = cliente.criadoEm();
        entity.atualizadoEm = cliente.atualizadoEm();
        return entity;
    }

    Cliente toDomain() {
        return Cliente.restaurar(id, new Documento(documento), nome, email, telefone, toEndereco(), criadoEm,
                atualizadoEm);
    }

    private Endereco toEndereco() {
        boolean semEndereco = logradouro == null && numero == null && complemento == null && bairro == null
                && cidade == null && uf == null && cep == null;
        return semEndereco ? null : new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }
}
