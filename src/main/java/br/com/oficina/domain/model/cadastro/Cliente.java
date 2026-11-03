package br.com.oficina.domain.model.cadastro;

import br.com.oficina.domain.valueobject.Documento;

import java.util.Objects;

public class Cliente {

    private final Integer id;
    private final Integer usuarioId;
    private final Documento documento;

    private Cliente(Integer id, Integer usuarioId, Documento documento) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.documento = documento;
    }

    public static Cliente cadastrar(Integer usuarioId, Documento documento) {
        return new Cliente(null, Objects.requireNonNull(usuarioId, "usuarioId"),
                Objects.requireNonNull(documento, "documento"));
    }

    public static Cliente restaurar(Integer id, Integer usuarioId, Documento documento) {
        return new Cliente(id, usuarioId, documento);
    }

    public Cliente comId(Integer novoId) {
        return new Cliente(novoId, usuarioId, documento);
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
}
