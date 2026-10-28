package br.com.oficina.identidade.application.dto;

import br.com.oficina.identidade.domain.Usuario;

import java.time.Instant;

public record UsuarioOutput(Integer id, String nome, String email, String telefone, String perfil,
                            Instant criadoEm) {

    public static UsuarioOutput from(Usuario usuario) {
        return new UsuarioOutput(usuario.id(), usuario.nome(), usuario.email(), usuario.telefone(),
                usuario.perfil().name(), usuario.criadoEm());
    }
}
