package br.com.oficina.infrastructure.dataproviders.database.entities.cadastro;

import br.com.oficina.domain.model.cadastro.Funcionario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_funcionarios")
public class FuncionarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false, unique = true)
    private Integer usuarioId;

    @Column(nullable = false)
    private boolean ativo;

    protected FuncionarioEntity() {
    }

    public static FuncionarioEntity from(Funcionario funcionario) {
        var entity = new FuncionarioEntity();
        entity.id = funcionario.id();
        entity.usuarioId = funcionario.usuarioId();
        entity.ativo = funcionario.ativo();
        return entity;
    }

    public Funcionario toDomain() {
        return new Funcionario(id, usuarioId, ativo);
    }
}
