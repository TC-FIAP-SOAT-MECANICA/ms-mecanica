package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.AvaliarOrcamentoInput;
import br.com.oficina.application.dto.atendimento.ItemPecaInput;
import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.atendimento.CatalogoGateway;
import br.com.oficina.application.gateways.atendimento.EstoqueGateway;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.gateways.shared.UnitOfWork;
import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.OrdemServicoPecaInsumo;
import br.com.oficina.domain.model.atendimento.StatusAprovacao;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EstoqueDaOrdemServicoTest {

    private static final Integer PECA = 200;
    private static final Instant AGORA = Instant.parse("2026-11-02T12:00:00Z");
    private static final Solicitante ADMIN = new Solicitante(1, Solicitante.Papel.ADMIN, null);

    private final FakeEstoque estoque = new FakeEstoque();
    private final InMemoryOrdemServicoRepository repository = new InMemoryOrdemServicoRepository();
    private final CatalogoGateway catalogo = servicoId -> Dinheiro.de("120.00");
    private final UnitOfWork unitOfWork = new DirectUnitOfWork();
    private final IncluirPecaNaOrdemUseCaseImpl incluirPeca =
            new IncluirPecaNaOrdemUseCaseImpl(repository, catalogo, estoque, unitOfWork);
    private final RemoverPecaDaOrdemUseCaseImpl removerPeca =
            new RemoverPecaDaOrdemUseCaseImpl(repository, estoque, unitOfWork);
    private final AvaliarOrcamentoUseCaseImpl avaliarOrcamento = new AvaliarOrcamentoUseCaseImpl(repository, estoque,
            unitOfWork, Clock.fixed(AGORA, ZoneOffset.UTC));

    @Test
    void ordemRecebidaNaoMexeNoEstoque() {
        Integer osId = abrirOrdem();

        OrdemServicoOutput os = incluirPeca.execute(osId, new ItemPecaInput(PECA, new BigDecimal("2")));
        removerPeca.execute(osId, os.pecas().getFirst().id());

        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("10");
    }

    @Test
    void aprovacaoColocaEmExecucaoEBaixaAsPecas() {
        Integer osId = abrirOrdem();
        incluirPeca.execute(osId, new ItemPecaInput(PECA, new BigDecimal("2")));
        enviarOrcamento(osId);
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("10");

        OrdemServicoOutput os = avaliarOrcamento.execute(new AvaliarOrcamentoInput(osId, true, null), ADMIN);

        assertThat(os.status()).isEqualTo(StatusOrdemServico.EM_EXECUCAO.name());
        assertThat(os.statusAprovacaoRejeicao()).isEqualTo(StatusAprovacao.APROVADO.name());
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("8");
    }

    @Test
    void rejeicaoFinalizaSemBaixarEstoque() {
        Integer osId = abrirOrdem();
        incluirPeca.execute(osId, new ItemPecaInput(PECA, new BigDecimal("2")));
        enviarOrcamento(osId);

        OrdemServicoOutput os = avaliarOrcamento.execute(new AvaliarOrcamentoInput(osId, false, null), ADMIN);

        assertThat(os.status()).isEqualTo(StatusOrdemServico.FINALIZADA.name());
        assertThat(os.statusAprovacaoRejeicao()).isEqualTo(StatusAprovacao.REJEITADO.name());
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("10");
    }

    @Test
    void ordemEmExecucaoBaixaNaInclusaoEDevolveNaRemocao() {
        Integer osId = abrirOrdem();
        enviarOrcamento(osId);
        avaliarOrcamento.execute(new AvaliarOrcamentoInput(osId, true, null), ADMIN);

        OrdemServicoOutput os = incluirPeca.execute(osId, new ItemPecaInput(PECA, new BigDecimal("3")));
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("7");

        removerPeca.execute(osId, os.pecas().getFirst().id());
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("10");
    }

    @Test
    void aprovacaoFalhaSemSaldoEOrdemContinuaAguardando() {
        Integer osId = abrirOrdem();
        incluirPeca.execute(osId, new ItemPecaInput(PECA, new BigDecimal("50")));
        enviarOrcamento(osId);

        assertThatThrownBy(() -> avaliarOrcamento.execute(new AvaliarOrcamentoInput(osId, true, null), ADMIN))
                .isInstanceOf(BusinessRuleException.class)
                .extracting("code").isEqualTo("ESTOQUE_INSUFICIENTE");
        assertThat(repository.findById(osId).orElseThrow().status())
                .isEqualTo(StatusOrdemServico.AGUARDANDO_APROVACAO);
        assertThat(estoque.saldo(PECA)).isEqualByComparingTo("10");
    }

    @Test
    void clienteNaoAvaliaOrcamentoDeOutroCliente() {
        Integer osId = abrirOrdem();
        enviarOrcamento(osId);
        Solicitante outroCliente = new Solicitante(50, Solicitante.Papel.CLIENTE, 99);

        assertThatThrownBy(() -> avaliarOrcamento.execute(new AvaliarOrcamentoInput(osId, true, null), outroCliente))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Integer abrirOrdem() {
        OrdemServico os = OrdemServico.abrir(1, 10, "Barulho", null, AGORA);
        os.incluirServico(100, Dinheiro.de("120.00"));
        return repository.save(os).id();
    }

    private void enviarOrcamento(Integer osId) {
        OrdemServico os = repository.findById(osId).orElseThrow();
        os.iniciarDiagnostico(7);
        os.registrarDiagnostico("Troca de filtro");
        repository.save(os);
    }

    private static class FakeEstoque implements EstoqueGateway {

        private final Map<Integer, BigDecimal> saldos = new HashMap<>(Map.of(PECA, BigDecimal.TEN));

        BigDecimal saldo(Integer pecaId) {
            return saldos.get(pecaId);
        }

        @Override
        public Dinheiro cotar(Integer pecaInsumoId, Quantidade quantidade) {
            return Dinheiro.de("35.50");
        }

        @Override
        public Dinheiro baixar(Integer pecaInsumoId, Quantidade quantidade) {
            BigDecimal saldo = saldos.get(pecaInsumoId);
            if (saldo.compareTo(quantidade.valor()) < 0) {
                throw new BusinessRuleException("ESTOQUE_INSUFICIENTE", "Estoque insuficiente");
            }
            saldos.put(pecaInsumoId, saldo.subtract(quantidade.valor()));
            return Dinheiro.de("35.50");
        }

        @Override
        public void devolver(Integer pecaInsumoId, Quantidade quantidade) {
            saldos.merge(pecaInsumoId, quantidade.valor(), BigDecimal::add);
        }
    }

    private static class DirectUnitOfWork implements UnitOfWork {

        @Override
        public <T> T execute(Supplier<T> work) {
            return work.get();
        }
    }

    private static class InMemoryOrdemServicoRepository implements OrdemServicoRepository {

        private final Map<Integer, OrdemServico> salvas = new HashMap<>();
        private int proximoItem = 1;

        @Override
        public OrdemServico save(OrdemServico os) {
            Integer id = os.id() == null ? salvas.size() + 1 : os.id();
            List<OrdemServicoPecaInsumo> pecas = new ArrayList<>();
            for (var peca : os.pecas()) {
                pecas.add(peca.id() != null ? peca : new OrdemServicoPecaInsumo(proximoItem++, peca.pecaInsumoId(),
                        peca.quantidade(), peca.precoUnitario(), peca.subtotal()));
            }
            OrdemServico salva = OrdemServico.restaurar(id, os.clienteId(), os.veiculoId(), os.mecanicoId(),
                    os.status(), os.descricaoProblema(), os.diagnosticoTecnico(), os.agendamentoDiagnostico(), os.agendamentoServico(),
                    os.statusAprovacaoRejeicao(), os.dataAprovacaoRejeicao(),
                    os.dataAbertura(), os.dataConclusao(), os.servicos(), pecas);
            salvas.put(id, salva);
            return salva;
        }

        @Override
        public Optional<OrdemServico> findById(Integer id) {
            OrdemServico os = salvas.get(id);
            return Optional.ofNullable(os == null ? null : OrdemServico.restaurar(os.id(), os.clienteId(),
                    os.veiculoId(), os.mecanicoId(), os.status(), os.descricaoProblema(), os.diagnosticoTecnico(),
                    os.agendamentoDiagnostico(), os.agendamentoServico(),
                    os.statusAprovacaoRejeicao(), os.dataAprovacaoRejeicao(), os.dataAbertura(), os.dataConclusao(), os.servicos(),
                    os.pecas()));
        }

        @Override
        public PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId,
                                                Integer veiculoId, PageQuery pageQuery) {
            return new PageResult<>(List.copyOf(salvas.values()), 0, salvas.size(), salvas.size(), 1);
        }
    }
}
