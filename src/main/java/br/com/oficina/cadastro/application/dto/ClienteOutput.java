package br.com.oficina.cadastro.application.dto;

import br.com.oficina.cadastro.domain.Cliente;

import java.time.Instant;
import java.util.UUID;

/** Dados de saída dos casos de uso de cliente. A entidade {@link Cliente} não sai da camada de aplicação. */
public record ClienteOutput(UUID id, String tipoDocumento, String documento, String nome, String email,
                            String telefone, EnderecoData endereco, Instant criadoEm, Instant atualizadoEm) {

    public static ClienteOutput from(Cliente cliente) {
        return new ClienteOutput(cliente.id(), cliente.documento().tipo().name(), cliente.documento().valor(),
                cliente.nome(), cliente.email(), cliente.telefone(), EnderecoData.from(cliente.endereco()),
                cliente.criadoEm(), cliente.atualizadoEm());
    }
}
