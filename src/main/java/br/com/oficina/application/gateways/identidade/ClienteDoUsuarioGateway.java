package br.com.oficina.application.gateways.identidade;

import java.util.Optional;

public interface ClienteDoUsuarioGateway {

    Optional<Integer> clienteIdDoUsuario(Integer usuarioId);
}
