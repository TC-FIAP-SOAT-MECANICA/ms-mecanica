package br.com.oficina.infrastructure.dataproviders.messaging.listener.cadastro;

import br.com.oficina.application.usecase.cadastro.ContratarFuncionarioUseCase;
import br.com.oficina.domain.model.identidade.UsuarioCadastrado;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class UsuarioCadastradoListener {

    private final ContratarFuncionarioUseCase contratarFuncionario;

    UsuarioCadastradoListener(ContratarFuncionarioUseCase contratarFuncionario) {
        this.contratarFuncionario = contratarFuncionario;
    }

    @EventListener
    void aoCadastrarUsuario(UsuarioCadastrado evento) {
        if (evento.funcionario()) {
            contratarFuncionario.execute(evento.usuarioId());
        }
    }
}
