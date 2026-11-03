package br.com.oficina.application.gateways.identidade;

import br.com.oficina.domain.model.identidade.UsuarioCadastrado;

public interface UsuarioEventPublisher {

    void publicar(UsuarioCadastrado evento);
}
