package br.com.oficina.application.dto.shared;

import java.util.Objects;

public record Solicitante(Integer usuarioId, Papel papel, Integer clienteId) {

    public enum Papel {
        ADMIN,
        MECANICO,
        CLIENTE
    }

    public Solicitante {
        Objects.requireNonNull(usuarioId, "usuarioId");
        Objects.requireNonNull(papel, "papel");
        if (papel == Papel.CLIENTE && clienteId == null) {
            throw new IllegalArgumentException("Solicitante CLIENTE precisa do clienteId");
        }
    }

    public boolean isCliente() {
        return papel == Papel.CLIENTE;
    }

    public boolean podeVerCliente(Integer outroClienteId) {
        return !isCliente() || clienteId.equals(outroClienteId);
    }

    public Integer restringirClienteId(Integer filtroClienteId) {
        return isCliente() ? clienteId : filtroClienteId;
    }
}
