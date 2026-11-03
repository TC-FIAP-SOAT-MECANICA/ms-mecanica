package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdemServicoTest {

    private static final Instant AGORA = Instant.parse("2026-10-25T12:00:00Z");
    private static final Instant AGENDA_DIAGNOSTICO = Instant.parse("2026-10-26T09:00:00Z");
    private static final Instant AGENDA_SERVICO = Instant.parse("2026-10-28T09:00:00Z");
    private static final Integer MECANICO = 7;

    private static OrdemServico novaOrdem() {
        return OrdemServico.abrir(1, 2, "Barulho", AGENDA_DIAGNOSTICO, AGORA);
    }

    private static OrdemServico aguardandoAprovacao() {
        OrdemServico os = novaOrdem();
        os.incluirServico(10, Dinheiro.de("120.00"));
        os.iniciarDiagnostico(MECANICO);
        os.registrarDiagnostico("Filtro saturado");
        return os;
    }

    @Test
    void abreComoRecebidaComAgendamentoDoDiagnostico() {
        OrdemServico os = novaOrdem();

        assertThat(os.status()).isEqualTo(StatusOrdemServico.RECEBIDA);
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.ZERO);
        assertThat(os.dataAbertura()).isEqualTo(AGORA);
        assertThat(os.agendamentoDiagnostico()).isEqualTo(AGENDA_DIAGNOSTICO);
        assertThat(os.mecanicoId()).isNull();
        assertThat(os.statusAprovacaoRejeicao()).isNull();
    }

    @Test
    void valorTotalSomaServicosEPecas() {
        OrdemServico os = novaOrdem();

        os.incluirServico(10, Dinheiro.de("120.00"));
        os.incluirPeca(20, Quantidade.de(new BigDecimal("1.5")), Dinheiro.de("35.50"));

        assertThat(os.pecas().getFirst().subtotal()).isEqualTo(Dinheiro.de("53.25"));
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.de("173.25"));
    }

    @Test
    void cicloCompletoComOrcamentoAprovado() {
        OrdemServico os = novaOrdem();
        os.incluirServico(10, Dinheiro.de("120.00"));

        os.iniciarDiagnostico(MECANICO);
        assertThat(os.status()).isEqualTo(StatusOrdemServico.EM_DIAGNOSTICO);
        os.registrarDiagnostico("Filtro saturado");
        assertThat(os.status()).isEqualTo(StatusOrdemServico.AGUARDANDO_APROVACAO);
        assertThat(os.statusAprovacaoRejeicao()).isEqualTo(StatusAprovacao.PENDENTE);
        os.aprovarOrcamento(AGENDA_SERVICO, AGORA.plusSeconds(60));
        assertThat(os.status()).isEqualTo(StatusOrdemServico.EM_EXECUCAO);
        os.finalizar(AGORA.plusSeconds(3600));
        os.entregar();

        assertThat(os.status()).isEqualTo(StatusOrdemServico.ENTREGUE);
        assertThat(os.mecanicoId()).isEqualTo(MECANICO);
        assertThat(os.diagnosticoTecnico()).isEqualTo("Filtro saturado");
        assertThat(os.statusAprovacaoRejeicao()).isEqualTo(StatusAprovacao.APROVADO);
        assertThat(os.dataAprovacaoRejeicao()).isEqualTo(AGORA.plusSeconds(60));
        assertThat(os.agendamentoServico()).isEqualTo(AGENDA_SERVICO);
        assertThat(os.dataConclusao()).isEqualTo(AGORA.plusSeconds(3600));
    }

    @Test
    void orcamentoRejeitadoFinalizaSemExecucao() {
        OrdemServico os = aguardandoAprovacao();

        os.rejeitarOrcamento(AGORA.plusSeconds(60));

        assertThat(os.status()).isEqualTo(StatusOrdemServico.FINALIZADA);
        assertThat(os.statusAprovacaoRejeicao()).isEqualTo(StatusAprovacao.REJEITADO);
        assertThat(os.dataAprovacaoRejeicao()).isEqualTo(AGORA.plusSeconds(60));
        assertThat(os.dataConclusao()).isEqualTo(AGORA.plusSeconds(60));
        os.entregar();
        assertThat(os.status()).isEqualTo(StatusOrdemServico.ENTREGUE);
    }

    @Test
    void recusaTransicaoForaDeOrdem() {
        OrdemServico os = novaOrdem();

        assertThatThrownBy(() -> os.registrarDiagnostico("Sem iniciar"))
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("OS_TRANSICAO_INVALIDA");
        assertThatThrownBy(() -> os.aprovarOrcamento(null, AGORA))
                .isInstanceOf(ConflictException.class);
        assertThatThrownBy(os::entregar)
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void naoEnviaOrcamentoSemServico() {
        OrdemServico os = novaOrdem();
        os.iniciarDiagnostico(MECANICO);

        assertThatThrownBy(() -> os.registrarDiagnostico("Sem defeito aparente"))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("ORDEM_SEM_SERVICOS");
        assertThat(os.status()).isEqualTo(StatusOrdemServico.EM_DIAGNOSTICO);
    }

    @Test
    void naoAlteraItensAguardandoAprovacaoNemDepoisDeFinalizada() {
        OrdemServico os = aguardandoAprovacao();

        assertThatThrownBy(() -> os.incluirServico(11, Dinheiro.de("10.00")))
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("OS_ITEM_NAO_EDITAVEL");

        os.aprovarOrcamento(null, AGORA);
        os.incluirServico(11, Dinheiro.de("10.00"));
        os.finalizar(AGORA);

        assertThatThrownBy(() -> os.incluirServico(12, Dinheiro.de("10.00")))
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("OS_ITEM_NAO_EDITAVEL");
    }

    @Test
    void removerPecaRecalculaTotalEDevolveOItem() {
        OrdemServico os = OrdemServico.restaurar(1, 1, 2, null, StatusOrdemServico.RECEBIDA, null, null, null, null, null,
                null, AGORA, null, List.of(new OrdemServicoServico(5, 10, Dinheiro.de("100.00"))),
                List.of(new OrdemServicoPecaInsumo(7, 20, Quantidade.de(BigDecimal.TWO), Dinheiro.de("10.00"),
                        Dinheiro.de("20.00"))));

        OrdemServicoPecaInsumo removido = os.removerPeca(7);

        assertThat(removido.pecaInsumoId()).isEqualTo(20);
        assertThat(os.valorTotal()).isEqualTo(Dinheiro.de("100.00"));
    }
}
