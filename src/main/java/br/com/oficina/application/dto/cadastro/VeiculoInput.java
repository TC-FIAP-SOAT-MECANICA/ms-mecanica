package br.com.oficina.application.dto.cadastro;

import br.com.oficina.domain.model.cadastro.DadosVeiculo;
import br.com.oficina.domain.model.cadastro.Placa;

public record VeiculoInput(Integer clienteId, String placa, String marca, String modelo, Integer ano, String cor,
                           String chassi, Integer quilometragem) {

    public DadosVeiculo toDados() {
        return new DadosVeiculo(clienteId, new Placa(placa), marca, modelo, ano, cor, chassi, quilometragem);
    }
}
