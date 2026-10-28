package br.com.oficina.identidade.application.dto;

public record AtualizarContatoInput(Integer usuarioId, String nome, String email, String telefone) {
}
