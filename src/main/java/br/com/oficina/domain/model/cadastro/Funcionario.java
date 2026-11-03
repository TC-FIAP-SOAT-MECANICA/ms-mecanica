package br.com.oficina.domain.model.cadastro;

import java.util.Objects;

public record Funcionario(Integer id, Integer usuarioId, boolean ativo) {

    public Funcionario {
        Objects.requireNonNull(usuarioId, "usuarioId");
    }

    public static Funcionario contratar(Integer usuarioId) {
        return new Funcionario(null, usuarioId, true);
    }
}
