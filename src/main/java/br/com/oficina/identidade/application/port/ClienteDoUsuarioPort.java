package br.com.oficina.identidade.application.port;

import java.util.Optional;

public interface ClienteDoUsuarioPort {

    Optional<Integer> clienteIdDoUsuario(Integer usuarioId);
}
