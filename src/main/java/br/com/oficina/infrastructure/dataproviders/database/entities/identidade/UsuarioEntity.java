package br.com.oficina.infrastructure.dataproviders.database.entities.identidade;

import br.com.oficina.domain.model.identidade.Perfil;
import br.com.oficina.domain.model.identidade.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil;

    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    protected UsuarioEntity() {
    }

    public static UsuarioEntity from(Usuario usuario) {
        var entity = new UsuarioEntity();
        entity.id = usuario.id();
        entity.nome = usuario.nome();
        entity.email = usuario.email();
        entity.telefone = usuario.telefone();
        entity.senhaHash = usuario.senhaHash();
        entity.perfil = usuario.perfil();
        entity.criadoEm = usuario.criadoEm();
        return entity;
    }

    public Usuario toDomain() {
        return Usuario.restaurar(id, nome, email, telefone, senhaHash, perfil, criadoEm);
    }
}
