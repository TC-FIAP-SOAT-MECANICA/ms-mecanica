package br.com.oficina.atendimento.application.port;

import java.util.Optional;

public interface CadastroGateway {

    boolean clienteExiste(Integer clienteId);

    Optional<Integer> clienteDoVeiculo(Integer veiculoId);
}
