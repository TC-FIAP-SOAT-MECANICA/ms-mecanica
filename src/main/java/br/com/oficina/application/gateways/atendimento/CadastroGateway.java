package br.com.oficina.application.gateways.atendimento;

import java.util.Optional;

public interface CadastroGateway {

    boolean clienteExiste(Integer clienteId);

    Optional<Integer> clienteDoVeiculo(Integer veiculoId);
}
