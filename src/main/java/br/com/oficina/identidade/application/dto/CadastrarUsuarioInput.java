package br.com.oficina.identidade.application.dto;

public record CadastrarUsuarioInput(String nome, String email, String telefone, String senha, String perfil) {
}
