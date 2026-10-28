package br.com.oficina.cadastro.domain;

import br.com.oficina.shared.domain.valueobject.Documento;

import java.time.Instant;
import java.util.Objects;

public class Cliente {

    private final Integer id;
    private final Integer usuarioId;
    private final Documento documento;
    private String nome;
    private String email;
    private String telefone;
    private final Instant criadoEm;

    private Cliente(Integer id, Integer usuarioId, Documento documento, String nome, String email, String telefone,
                    Instant criadoEm) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.documento = documento;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.criadoEm = criadoEm;
    }

    public static Cliente cadastrar(Integer usuarioId, Documento documento, String nome, String email,
                                    String telefone, Instant criadoEm) {
        return new Cliente(null, Objects.requireNonNull(usuarioId, "usuarioId"),
                Objects.requireNonNull(documento, "documento"), nome, email, telefone, criadoEm);
    }

    public static Cliente restaurar(Integer id, Integer usuarioId, Documento documento, String nome, String email,
                                    String telefone, Instant criadoEm) {
        return new Cliente(id, usuarioId, documento, nome, email, telefone, criadoEm);
    }

    public Cliente comId(Integer novoId) {
        return new Cliente(novoId, usuarioId, documento, nome, email, telefone, criadoEm);
    }

    public void atualizarContato(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Integer id() {
        return id;
    }

    public Integer usuarioId() {
        return usuarioId;
    }

    public Documento documento() {
        return documento;
    }

    public String nome() {
        return nome;
    }

    public String email() {
        return email;
    }

    public String telefone() {
        return telefone;
    }

    public Instant criadoEm() {
        return criadoEm;
    }
}
