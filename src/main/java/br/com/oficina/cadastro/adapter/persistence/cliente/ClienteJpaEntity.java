package br.com.oficina.cadastro.adapter.persistence.cliente;

import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.adapter.persistence.crypto.CampoCriptografadoConverter;
import br.com.oficina.shared.domain.valueobject.Documento;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
class ClienteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Integer usuarioId;

    @Convert(converter = CampoCriptografadoConverter.class)
    @Column(name = "cpf_cnpj", nullable = false)
    private String cpfCnpj;

    @Column(name = "cpf_cnpj_hash", unique = true, length = 64)
    private String cpfCnpjHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private ContatoClienteJpaEntity contato;

    protected ClienteJpaEntity() {
    }

    static ClienteJpaEntity from(Cliente cliente, String cpfCnpjHash) {
        var entity = new ClienteJpaEntity();
        entity.id = cliente.id();
        entity.usuarioId = cliente.usuarioId();
        entity.cpfCnpj = cliente.documento().valor();
        entity.cpfCnpjHash = cpfCnpjHash;
        return entity;
    }

    Integer id() {
        return id;
    }

    Cliente toDomain() {
        return Cliente.restaurar(id, usuarioId, new Documento(cpfCnpj), contato.nome(), contato.email(),
                contato.telefone(), contato.criadoEm());
    }
}
