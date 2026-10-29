package br.com.oficina.application.dto.identidade;

public record TokenOutput(String accessToken, long expiresIn, Integer usuarioId, String perfil, Integer clienteId) {
}
