package br.com.oficina.application.dto.identidade;

public record AtualizarContatoInput(Integer usuarioId, String nome, String email, String telefone) {
}
