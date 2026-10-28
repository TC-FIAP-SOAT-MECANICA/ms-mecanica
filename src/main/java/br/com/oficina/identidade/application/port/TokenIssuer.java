package br.com.oficina.identidade.application.port;

import br.com.oficina.identidade.domain.Usuario;

public interface TokenIssuer {

    TokenEmitido emitir(Usuario usuario, Integer clienteId);

    record TokenEmitido(String valor, long expiraEmSegundos) {
    }
}
