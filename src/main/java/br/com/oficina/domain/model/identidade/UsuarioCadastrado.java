package br.com.oficina.domain.model.identidade;

public record UsuarioCadastrado(Integer usuarioId, boolean funcionario) {

    public static UsuarioCadastrado de(Usuario usuario) {
        return new UsuarioCadastrado(usuario.id(), usuario.perfil().funcionario());
    }
}
