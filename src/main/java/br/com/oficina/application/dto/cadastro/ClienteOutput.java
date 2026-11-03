package br.com.oficina.application.dto.cadastro;

import br.com.oficina.application.gateways.cadastro.UsuarioGateway.ContatoUsuario;
import br.com.oficina.domain.model.cadastro.Cliente;

import java.time.Instant;

public record ClienteOutput(Integer id, Integer usuarioId, String tipoDocumento, String documento, String nome,
                            String email, String telefone, Instant criadoEm) {

    public static ClienteOutput from(Cliente cliente, ContatoUsuario contato) {
        return new ClienteOutput(cliente.id(), cliente.usuarioId(), cliente.documento().tipo().name(),
                cliente.documento().valor(), contato.nome(), contato.email(), contato.telefone(),
                contato.criadoEm());
    }
}
