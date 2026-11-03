package br.com.oficina.domain.valueobject;

import br.com.oficina.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DinheiroQuantidadeTest {

    @Test
    void dinheiroArredondaParaDuasCasasESoma() {
        Dinheiro total = Dinheiro.de("10.005").somar(Dinheiro.de("5"));

        assertThat(total).hasToString("15.00");
        assertThat(Dinheiro.ZERO.valor()).isEqualByComparingTo("0");
    }

    @Test
    void dinheiroMultiplicaPelaQuantidade() {
        assertThat(Dinheiro.de("35.50").multiplicar(Quantidade.de(new BigDecimal("1.5")))).hasToString("53.25");
    }

    @Test
    void dinheiroRecusaNegativoEAusente() {
        assertThatThrownBy(() -> Dinheiro.de("-0.01")).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> new Dinheiro(null)).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Dinheiro.de(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Valor monetário é obrigatório");
    }

    @Test
    void quantidadeArredondaEExigeValorPositivo() {
        assertThat(Quantidade.de(new BigDecimal("2.345"))).hasToString("2.34");
        assertThatThrownBy(() -> Quantidade.de(BigDecimal.ZERO)).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Quantidade.de(new BigDecimal("-1"))).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> Quantidade.de(null)).isInstanceOf(ValidationException.class);
    }
}
