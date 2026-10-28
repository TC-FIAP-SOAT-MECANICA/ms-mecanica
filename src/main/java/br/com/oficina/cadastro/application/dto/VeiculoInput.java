package br.com.oficina.cadastro.application.dto;

import br.com.oficina.cadastro.domain.DadosVeiculo;
import br.com.oficina.cadastro.domain.Placa;

public record VeiculoInput(Integer clienteId, String placa, String marca, String modelo, Integer ano, String cor,
                           String chassi, Integer quilometragem) {

    public DadosVeiculo toDados() {
        return new DadosVeiculo(clienteId, new Placa(placa), marca, modelo, ano, cor, chassi, quilometragem);
    }
}
