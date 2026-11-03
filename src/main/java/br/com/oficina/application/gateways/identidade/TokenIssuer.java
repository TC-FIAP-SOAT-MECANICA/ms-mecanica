package br.com.oficina.application.gateways.identidade;

import br.com.oficina.domain.model.identidade.Usuario;

public interface TokenIssuer {

    TokenEmitido emitir(Usuario usuario, Integer clienteId);

    record TokenEmitido(String valor, long expiraEmSegundos) {
    }
}
