package br.com.oficina.cadastro.application.dto;

public record AtualizarClienteInput(Integer clienteId, String nome, String email, String telefone) {
}
