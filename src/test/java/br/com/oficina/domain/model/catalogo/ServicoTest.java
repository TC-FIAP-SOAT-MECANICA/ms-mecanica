package br.com.oficina.domain.model.catalogo;

import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.domain.valueobject.Dinheiro;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServicoTest {

    @Test
    void criaComNomeEDescricaoNormalizados() {
        Servico servico = Servico.criar(" Troca de óleo ", " Óleo 5W30 ", Dinheiro.de("120.00"));

        assertThat(servico.nome()).isEqualTo("Troca de óleo");
        assertThat(servico.descricao()).isEqualTo("Óleo 5W30");
        assertThat(servico.precoBase()).isEqualTo(Dinheiro.de("120"));
    }

    @Test
    void descricaoEmBrancoViraNula() {
        Servico servico = Servico.restaurar(1, "Alinhamento", "Antiga", Dinheiro.de("80.00"));

        servico.atualizar("Alinhamento", "  ", Dinheiro.de("90.00"));

        assertThat(servico.id()).isEqualTo(1);
        assertThat(servico.descricao()).isNull();
        assertThat(servico.precoBase()).isEqualTo(Dinheiro.de("90.00"));
        servico.atualizar("Alinhamento", null, Dinheiro.de("90.00"));
        assertThat(servico.descricao()).isNull();
    }

    @Test
    void exigeNomeEPreco() {
        assertThatThrownBy(() -> Servico.criar(" ", null, Dinheiro.de("10.00")))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Servico.criar(null, null, Dinheiro.de("10.00")))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Servico.criar("Balanceamento", null, null))
                .isInstanceOf(NullPointerException.class);
    }
}
