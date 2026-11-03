package br.com.oficina.application.gateways.atendimento;

import java.util.Optional;

public interface FuncionarioGateway {

    Optional<FuncionarioAtendente> funcionarioDoUsuario(Integer usuarioId);

    record FuncionarioAtendente(Integer id, boolean ativo) {
    }
}
