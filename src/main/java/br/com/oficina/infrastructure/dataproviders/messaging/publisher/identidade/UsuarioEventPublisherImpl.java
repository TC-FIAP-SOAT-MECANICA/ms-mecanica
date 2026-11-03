package br.com.oficina.infrastructure.dataproviders.messaging.publisher.identidade;

import br.com.oficina.application.gateways.identidade.UsuarioEventPublisher;
import br.com.oficina.domain.model.identidade.UsuarioCadastrado;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class UsuarioEventPublisherImpl implements UsuarioEventPublisher {

    private final ApplicationEventPublisher publisher;

    UsuarioEventPublisherImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(UsuarioCadastrado evento) {
        publisher.publishEvent(evento);
    }
}
