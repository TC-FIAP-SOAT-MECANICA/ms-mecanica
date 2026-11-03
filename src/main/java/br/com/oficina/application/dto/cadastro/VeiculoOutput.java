package br.com.oficina.application.dto.cadastro;

import br.com.oficina.domain.model.cadastro.Veiculo;

public record VeiculoOutput(Integer id, Integer clienteId, String placa, String marca, String modelo, int ano,
                            String cor, String chassi, Integer quilometragem) {

    public static VeiculoOutput from(Veiculo veiculo) {
        return new VeiculoOutput(veiculo.id(), veiculo.clienteId(), veiculo.placa().valor(), veiculo.marca(),
                veiculo.modelo(), veiculo.ano(), veiculo.cor(), veiculo.chassi(), veiculo.quilometragem());
    }
}
