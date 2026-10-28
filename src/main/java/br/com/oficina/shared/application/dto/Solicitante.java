package br.com.oficina.shared.application.dto;

import java.util.Objects;

public record Solicitante(Integer usuarioId, Papel papel, Integer clienteId) {

    public enum Papel {
        ADMIN,
        MECANICO,
        CLIENTE,
        ANONIMO,
        SISTEMA
    }

    public Solicitante {
        Objects.requireNonNull(papel, "papel");
        if (papel == Papel.CLIENTE && clienteId == null) {
            throw new IllegalArgumentException("Solicitante CLIENTE precisa do clienteId");
        }
    }

    public static Solicitante sistema() {
        return new Solicitante(null, Papel.SISTEMA, null);
    }

    public static Solicitante anonimo() {
        return new Solicitante(null, Papel.ANONIMO, null);
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
