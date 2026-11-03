package br.com.oficina.domain.model.estoque;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PecaInsumoTest {

    private static PecaInsumo novaPeca() {
        return novaPeca("un", "5");
    }

    private static PecaInsumo novaPeca(String unidade, String estoqueMinimo) {
        return PecaInsumo.cadastrar(new DadosPecaInsumo("FLT-001", "Filtro de óleo", unidade, Dinheiro.de("20.00"),
                Dinheiro.de("35.50"), new BigDecimal(estoqueMinimo)));
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
        peca.darSaida(Quantidade.de(new BigDecimal("2")));

        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("8");
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

    @Test
    void unidadeContavelRecusaQuantidadeFracionada() {
        PecaInsumo peca = novaPeca();
        peca.darEntrada(Quantidade.de(BigDecimal.TEN));

        assertThatThrownBy(() -> peca.darSaida(Quantidade.de(new BigDecimal("2.5"))))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("QUANTIDADE_FRACIONADA");
        assertThatThrownBy(() -> peca.darEntrada(Quantidade.de(new BigDecimal("0.5"))))
                .isInstanceOf(BusinessRuleException.class);
        assertThatThrownBy(() -> peca.ajustarInventario(new BigDecimal("3.2")))
                .isInstanceOf(BusinessRuleException.class);
        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("10");
    }

    @Test
    void unidadeContavelAceitaInteiroComCasasDecimaisZeradas() {
        PecaInsumo peca = novaPeca();

        peca.darEntrada(Quantidade.de(new BigDecimal("3.00")));

        assertThat(peca.quantidadeEstoque()).isEqualByComparingTo("3");
    }

    @Test
    void unidadeFracionavelAceitaQuantidadeQuebrada() {
        PecaInsumo oleo = novaPeca("LT", "2.5");
        oleo.darEntrada(Quantidade.de(new BigDecimal("10.5")));

        oleo.darSaida(Quantidade.de(new BigDecimal("3.75")));

        assertThat(oleo.quantidadeEstoque()).isEqualByComparingTo("6.75");
    }

    @Test
    void unidadeContavelRecusaEstoqueMinimoFracionado() {
        assertThatThrownBy(() -> novaPeca("UN", "1.5"))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("QUANTIDADE_FRACIONADA");
    }

    @Test
    void naoTrocaParaUnidadeContavelComSaldoFracionado() {
        PecaInsumo oleo = novaPeca("LT", "1");
        oleo.darEntrada(Quantidade.de(new BigDecimal("2.5")));

        assertThatThrownBy(() -> oleo.atualizarCadastro(new DadosPecaInsumo("FLT-001", "Filtro de óleo", "UN",
                Dinheiro.de("20.00"), Dinheiro.de("35.50"), BigDecimal.ONE)))
                .isInstanceOf(BusinessRuleException.class);
        assertThat(oleo.unidadeMedida()).isEqualTo("LT");
    }
}
