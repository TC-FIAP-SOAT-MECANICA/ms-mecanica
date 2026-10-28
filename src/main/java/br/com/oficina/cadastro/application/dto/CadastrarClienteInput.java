package br.com.oficina.cadastro.application.dto;

public record CadastrarClienteInput(String documento, String nome, String email, String telefone, String senha) {
}
