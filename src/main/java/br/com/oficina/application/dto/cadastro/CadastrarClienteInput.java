package br.com.oficina.application.dto.cadastro;

public record CadastrarClienteInput(String documento, String nome, String email, String telefone, String senha) {
}
