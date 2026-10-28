package br.com.oficina.cadastro.application.port;

import java.time.Instant;

public interface UsuarioGateway {

    UsuarioCriado cadastrarUsuarioCliente(String nome, String email, String telefone, String senha);

    ContatoUsuario atualizarContato(Integer usuarioId, String nome, String email, String telefone);

    void removerUsuario(Integer usuarioId);

    record UsuarioCriado(Integer id, String nome, String email, String telefone, Instant criadoEm) {
    }

    record ContatoUsuario(String nome, String email, String telefone) {
    }
}
