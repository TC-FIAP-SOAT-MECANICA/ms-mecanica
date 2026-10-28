package br.com.oficina.cadastro.application.dto;

import br.com.oficina.cadastro.domain.Cliente;

import java.time.Instant;

public record ClienteOutput(Integer id, Integer usuarioId, String tipoDocumento, String documento, String nome,
                            String email, String telefone, Instant criadoEm) {

    public static ClienteOutput from(Cliente cliente) {
        return new ClienteOutput(cliente.id(), cliente.usuarioId(), cliente.documento().tipo().name(),
                cliente.documento().valor(), cliente.nome(), cliente.email(), cliente.telefone(),
                cliente.criadoEm());
    }
}
