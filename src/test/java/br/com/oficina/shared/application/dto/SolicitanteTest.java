package br.com.oficina.shared.application.dto;

import br.com.oficina.shared.application.dto.Solicitante.Papel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitanteTest {

    @Test
    void clienteSoVeOProprioCadastro() {
        Solicitante cliente = new Solicitante(10, Papel.CLIENTE, 3);

        assertThat(cliente.podeVerCliente(3)).isTrue();
        assertThat(cliente.podeVerCliente(4)).isFalse();
        assertThat(cliente.restringirClienteId(null)).isEqualTo(3);
        assertThat(cliente.restringirClienteId(4)).isEqualTo(3);
    }

    @Test
    void mecanicoEAdminVeemTodos() {
        Solicitante mecanico = new Solicitante(2, Papel.MECANICO, null);

        assertThat(mecanico.podeVerCliente(4)).isTrue();
        assertThat(mecanico.restringirClienteId(null)).isNull();
        assertThat(mecanico.restringirClienteId(4)).isEqualTo(4);
    }

    @Test
    void clienteExigeClienteId() {
        assertThatThrownBy(() -> new Solicitante(10, Papel.CLIENTE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
