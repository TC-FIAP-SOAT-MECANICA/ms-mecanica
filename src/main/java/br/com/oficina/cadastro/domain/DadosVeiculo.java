package br.com.oficina.cadastro.domain;

public record DadosVeiculo(Integer clienteId, Placa placa, String marca, String modelo, Integer ano, String cor,
                           String chassi, Integer quilometragem) {
}
