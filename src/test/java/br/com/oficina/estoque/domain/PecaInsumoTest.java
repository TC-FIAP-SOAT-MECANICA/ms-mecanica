package br.com.oficina.estoque.domain;

import br.com.oficina.shared.domain.exception.BusinessRuleException;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import br.com.oficina.shared.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PecaInsumoTest {

    private static PecaInsumo novaPeca() {
        return PecaInsumo.cadastrar(new DadosPecaInsumo("FLT-001", "Filtro de óleo", "un", Dinheiro.de("20.00"),
                Dinheiro.de("35.50"), new BigDecimal("5")));
    }

    @Test
    void nasceComSaldoZeroEAbaixoDoMinimo() {
        PecaInsumo peca = novaPeca();

        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("0");
        assertThat(peca.unidadeMedida()).isEqualTo("UN");
        assertThat(peca.abaixoDoMinimo()).isTrue();
    }

    @Test
    void entradaESaidaAtualizamOSaldo() {
        PecaInsumo peca = novaPeca();

        peca.darEntrada(Quantidade.de(new BigDecimal("10")));
        peca.darSaida(Quantidade.de(new BigDecimal("2.5")));

        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("7.50");
        assertThat(peca.abaixoDoMinimo()).isFalse();
    }

    @Test
    void recusaSaidaMaiorQueOSaldo() {
        PecaInsumo peca = novaPeca();
        peca.darEntrada(Quantidade.de(BigDecimal.ONE));

        assertThatThrownBy(() -> peca.darSaida(Quantidade.de(BigDecimal.TWO)))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("ESTOQUE_INSUFICIENTE");
        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("1");
    }

    @Test
    void ajusteDeInventarioDefineOSaldoContado() {
        PecaInsumo peca = novaPeca();
        peca.darEntrada(Quantidade.de(BigDecimal.TEN));

        peca.ajustarInventario(new BigDecimal("3"));

        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("3");
    }
}
