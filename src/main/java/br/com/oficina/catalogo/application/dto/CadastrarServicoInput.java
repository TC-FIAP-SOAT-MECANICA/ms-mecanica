package br.com.oficina.catalogo.application.dto;

/** Dados de entrada do caso de uso. Tipos simples: não depende de HTTP nem de JSON. */
public record CadastrarServicoInput(String nome, String descricao, String preco, int tempoEstimadoMinutos) {
}
