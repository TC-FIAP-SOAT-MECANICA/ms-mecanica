package br.com.oficina.application.dto.identidade;

import br.com.oficina.domain.model.identidade.Usuario;

import java.time.Instant;

public record UsuarioOutput(Integer id, String nome, String email, String telefone, String perfil,
                            Instant criadoEm) {

    public static UsuarioOutput from(Usuario usuario) {
        return new UsuarioOutput(usuario.id(), usuario.nome(), usuario.email(), usuario.telefone(),
                usuario.perfil().name(), usuario.criadoEm());
    }
}
