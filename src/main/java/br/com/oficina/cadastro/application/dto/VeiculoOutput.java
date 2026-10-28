package br.com.oficina.cadastro.application.dto;

import br.com.oficina.cadastro.domain.Veiculo;

public record VeiculoOutput(Integer id, Integer clienteId, String placa, String marca, String modelo, int ano,
                            String cor, String chassi, Integer quilometragem) {

    public static VeiculoOutput from(Veiculo veiculo) {
        return new VeiculoOutput(veiculo.id(), veiculo.clienteId(), veiculo.placa().valor(), veiculo.marca(),
                veiculo.modelo(), veiculo.ano(), veiculo.cor(), veiculo.chassi(), veiculo.quilometragem());
    }
}
