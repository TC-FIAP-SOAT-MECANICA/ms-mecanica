package br.com.oficina.identidade.application.dto;

public record TokenOutput(String accessToken, long expiresIn, Integer usuarioId, String perfil, Integer clienteId) {
}
