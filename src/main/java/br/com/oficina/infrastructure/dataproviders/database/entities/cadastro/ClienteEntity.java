package br.com.oficina.infrastructure.dataproviders.database.entities.cadastro;

import br.com.oficina.domain.model.cadastro.Cliente;
import br.com.oficina.domain.valueobject.Documento;
import br.com.oficina.infrastructure.dataproviders.database.crypto.CampoCriptografadoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

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

    protected ClienteEntity() {
    }

    public static ClienteEntity from(Cliente cliente, String cpfCnpjHash) {
        var entity = new ClienteEntity();
        entity.id = cliente.id();
        entity.usuarioId = cliente.usuarioId();
        entity.cpfCnpj = cliente.documento().valor();
        entity.cpfCnpjHash = cpfCnpjHash;
        return entity;
    }

    public Integer id() {
        return id;
    }

    public Cliente toDomain() {
        return Cliente.restaurar(id, usuarioId, new Documento(cpfCnpj));
    }
}
