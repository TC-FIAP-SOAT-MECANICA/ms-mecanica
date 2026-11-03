package br.com.oficina.domain.model.cadastro;

import br.com.oficina.domain.valueobject.Documento;

import java.util.Objects;

public class Cliente {

    private final Integer id;
    private final Integer usuarioId;
    private final Documento cpfCnpj;

    private Cliente(Integer id, Integer usuarioId, Documento cpfCnpj) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.cpfCnpj = cpfCnpj;
    }

    public static Cliente cadastrar(Integer usuarioId, Documento cpfCnpj) {
        return new Cliente(null, Objects.requireNonNull(usuarioId, "usuarioId"),
                Objects.requireNonNull(cpfCnpj, "cpfCnpj"));
    }

    public static Cliente restaurar(Integer id, Integer usuarioId, Documento cpfCnpj) {
        return new Cliente(id, usuarioId, cpfCnpj);
    }

    public Cliente comId(Integer novoId) {
        return new Cliente(novoId, usuarioId, cpfCnpj);
    }

    public Integer id() {
        return id;
    }

    public Integer usuarioId() {
        return usuarioId;
    }

    public Documento cpfCnpj() {
        return cpfCnpj;
    }
}
