package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdemServicoTest {

    private static final Instant AGORA = Instant.parse("2026-10-25T12:00:00Z");

    @Test
    void abreComStatusAbertaEValorZero() {
        OrdemServico os = OrdemServico.abrir(1, 2, "Barulho", AGORA);

        assertThat(os.status()).isEqualTo(StatusOrdemServico.ABERTA);
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.ZERO);
        assertThat(os.dataAbertura()).isEqualTo(AGORA);
    }

    @Test
    void valorTotalSomaServicosEPecas() {
        OrdemServico os = OrdemServico.abrir(1, 2, null, AGORA);

        os.incluirServico(10, Dinheiro.de("120.00"));
        os.incluirPeca(20, Quantidade.de(new BigDecimal("1.5")), Dinheiro.de("35.50"));

        assertThat(os.pecas().getFirst().subtotal()).isEqualTo(Dinheiro.de("53.25"));
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.de("173.25"));
    }

    @Test
    void cicloCompletoAteEntrega() {
        OrdemServico os = OrdemServico.abrir(1, 2, null, AGORA);
        os.incluirServico(10, Dinheiro.de("120.00"));

        os.registrarDiagnostico("Filtro saturado");
        os.finalizar(AGORA.plusSeconds(3600));
        os.entregar();

        assertThat(os.status()).isEqualTo(StatusOrdemServico.ENTREGUE);
        assertThat(os.diagnosticoTecnico()).isEqualTo("Filtro saturado");
        assertThat(os.dataConclusao()).isEqualTo(AGORA.plusSeconds(3600));
    }

    @Test
    void recusaTransicaoForaDeOrdem() {
        OrdemServico os = OrdemServico.abrir(1, 2, null, AGORA);

        assertThatThrownBy(os::entregar)
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("OS_TRANSICAO_INVALIDA");
    }

    @Test
    void naoConcluiSemServico() {
        OrdemServico os = OrdemServico.abrir(1, 2, null, AGORA);
        os.registrarDiagnostico("Sem defeito aparente");

        assertThatThrownBy(() -> os.finalizar(AGORA))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("ORDEM_SEM_SERVICOS");
    }

    @Test
    void naoAlteraItensDepoisDeConcluida() {
        OrdemServico os = OrdemServico.abrir(1, 2, null, AGORA);
        os.incluirServico(10, Dinheiro.de("120.00"));
        os.registrarDiagnostico("Ok");
        os.finalizar(AGORA);

        assertThatThrownBy(() -> os.incluirServico(11, Dinheiro.de("10.00")))
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("OS_ITEM_NAO_EDITAVEL");
    }

    @Test
    void removerPecaRecalculaTotalEDevolveOItem() {
        OrdemServico os = OrdemServico.restaurar(1, 1, 2, StatusOrdemServico.ABERTA, null, null, AGORA, null,
                java.util.List.of(new ItemServico(5, 10, Dinheiro.de("100.00"))),
                java.util.List.of(new ItemPeca(7, 20, Quantidade.de(BigDecimal.TWO), Dinheiro.de("10.00"),
                        Dinheiro.de("20.00"))));

        ItemPeca removido = os.removerPeca(7);

        assertThat(removido.pecaInsumoId()).isEqualTo(20);
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.de("100.00"));
    }
}
