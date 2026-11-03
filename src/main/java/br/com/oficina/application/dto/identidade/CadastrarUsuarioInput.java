package br.com.oficina.application.dto.identidade;

public record CadastrarUsuarioInput(String nome, String email, String telefone, String senha, String perfil) {
}
