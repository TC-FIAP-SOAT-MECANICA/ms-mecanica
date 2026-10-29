package br.com.oficina.application.dto.cadastro;

public record AtualizarClienteInput(Integer clienteId, String nome, String email, String telefone) {
}
