package br.com.oficina.integracao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrdemServicoIntegracaoTest extends IntegracaoTest {

    private String admin;
    private String mecanico;
    private ClienteCriado cliente;
    private Integer veiculoId;
    private Integer servicoId;
    private Integer pecaId;

    @BeforeEach
    void prepararCadastros() throws Exception {
        admin = tokenAdmin();
        mecanico = novoMecanico(admin);
        cliente = novoCliente();
        veiculoId = novoVeiculo(cliente.id(), cliente.token());
        servicoId = novoServico(admin, "120.00");
        pecaId = novaPeca(mecanico, "UN", 10);
    }

    @Test
    void fluxoCompletoDaAberturaAEntrega() throws Exception {
        ResultActions aberta = post("/ordens-servico", mecanico, """
                {"clienteId": %d, "veiculoId": %d, "descricaoProblema": "Barulho no motor",
                 "agendamentoDiagnostico": "2026-11-10T09:00:00-03:00",
                 "servicos": [{"servicoId": %d}], "pecas": [{"pecaInsumoId": %d, "quantidade": 2}]}"""
                .formatted(cliente.id(), veiculoId, servicoId, pecaId))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.status").value("RECEBIDA"))
                .andExpect(jsonPath("$.valorTotal").value("191.00"))
                .andExpect(jsonPath("$.agendamentoDiagnostico").isString());
        Integer osId = ler(aberta, "$.id");
        String os = "/ordens-servico/" + osId;

        get(os, cliente.token()).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("RECEBIDA"));
        post(os + "/diagnostico/inicio", mecanico, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_DIAGNOSTICO"))
                .andExpect(jsonPath("$.mecanicoId").isNumber());
        post(os + "/pecas", mecanico, """
                {"pecaInsumoId": %d, "quantidade": 1}""".formatted(pecaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value("226.50"));
        post(os + "/diagnostico", mecanico, """
                {"diagnosticoTecnico": "Filtro saturado"}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AGUARDANDO_APROVACAO"))
                .andExpect(jsonPath("$.statusAprovacaoRejeicao").value("PENDENTE"));
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(10));

        post(os + "/aprovacao", cliente.token(), """
                {"aprovado": true, "agendamentoServico": "2026-11-12T09:00:00-03:00"}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_EXECUCAO"))
                .andExpect(jsonPath("$.statusAprovacaoRejeicao").value("APROVADO"))
                .andExpect(jsonPath("$.dataAprovacaoRejeicao").isString())
                .andExpect(jsonPath("$.agendamentoServico").isString());
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(7));

        post(os + "/finalizacao", mecanico, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"))
                .andExpect(jsonPath("$.dataConclusao").isString());
        post(os + "/entrega", mecanico, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENTREGUE"));
        get("/ordens-servico?status=ENTREGUE", cliente.token())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(osId));
    }

    @Test
    void orcamentoRejeitadoFinalizaSemBaixarEstoque() throws Exception {
        String os = aguardandoAprovacao(2);

        post(os + "/aprovacao", admin, """
                {"aprovado": false}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"))
                .andExpect(jsonPath("$.statusAprovacaoRejeicao").value("REJEITADO"));
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(10));
        post(os + "/entrega", mecanico, null).andExpect(jsonPath("$.status").value("ENTREGUE"));
    }

    @Test
    void aprovacaoSemSaldoNaoAlteraAOrdem() throws Exception {
        String os = aguardandoAprovacao(50);

        post(os + "/aprovacao", admin, """
                {"aprovado": true}""")
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("ESTOQUE_INSUFICIENTE"));
        get(os, mecanico).andExpect(jsonPath("$.status").value("AGUARDANDO_APROVACAO"));
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(10));
    }

    @Test
    void itensDaOrdemEmCadaEtapa() throws Exception {
        String os = abrir("");

        ResultActions comPeca = post(os + "/pecas", mecanico, """
                {"pecaInsumoId": %d, "quantidade": 3}""".formatted(pecaId)).andExpect(status().isOk());
        Integer itemPeca = ler(comPeca, "$.pecas[0].id");
        ResultActions comServico = post(os + "/servicos", mecanico, """
                {"servicoId": %d, "precoFinal": "99.90"}""".formatted(servicoId)).andExpect(status().isOk());
        Integer itemServico = ler(comServico, "$.servicos[1].id");
        delete(os + "/servicos/" + itemServico, mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value("226.50"));
        delete(os + "/pecas/" + itemPeca, mecanico)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value("120.00"));
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(10));

        post(os + "/diagnostico/inicio", mecanico, null).andExpect(status().isOk());
        post(os + "/diagnostico", mecanico, """
                {"diagnosticoTecnico": "Revisão"}""").andExpect(status().isOk());
        post(os + "/pecas", mecanico, """
                {"pecaInsumoId": %d, "quantidade": 1}""".formatted(pecaId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("OS_ITEM_NAO_EDITAVEL"));
        post(os + "/aprovacao", cliente.token(), """
                {"aprovado": true}""").andExpect(status().isOk());

        ResultActions emExecucao = post(os + "/pecas", mecanico, """
                {"pecaInsumoId": %d, "quantidade": 4}""".formatted(pecaId)).andExpect(status().isOk());
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(6));
        Integer itemExecucao = ler(emExecucao, "$.pecas[0].id");
        delete(os + "/pecas/" + itemExecucao, mecanico).andExpect(status().isOk());
        saldo(pecaId).andExpect(jsonPath("$.quantidadeEstoque").value(10));
        delete(os + "/pecas/999999", mecanico).andExpect(status().isNotFound());
        delete(os + "/servicos/999999", mecanico).andExpect(status().isNotFound());
    }

    @Test
    void regrasDeTransicaoEValidacao() throws Exception {
        String os = abrir("");

        post(os + "/diagnostico", mecanico, """
                {"diagnosticoTecnico": "Sem iniciar"}""")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("OS_TRANSICAO_INVALIDA"));
        post(os + "/finalizacao", mecanico, null).andExpect(status().isConflict());
        post(os + "/entrega", mecanico, null).andExpect(status().isConflict());
        post(os + "/pecas", mecanico, """
                {"pecaInsumoId": %d, "quantidade": 1.5}""".formatted(pecaId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("QUANTIDADE_FRACIONADA"));
        post(os + "/servicos", mecanico, """
                {"servicoId": 999999}""").andExpect(status().isNotFound());
        get("/ordens-servico/999999", admin).andExpect(status().isNotFound());

        String semServico = abrirSemItens();
        post(semServico + "/diagnostico/inicio", mecanico, null).andExpect(status().isOk());
        post(semServico + "/diagnostico", mecanico, """
                {"diagnosticoTecnico": "Nada"}""")
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("ORDEM_SEM_SERVICOS"));
    }

    @Test
    void validaClienteEVeiculoNaAbertura() throws Exception {
        ClienteCriado outro = novoCliente();
        String corpo = """
                {"clienteId": %d, "veiculoId": %d}""";

        post("/ordens-servico", mecanico, corpo.formatted(outro.id(), veiculoId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.codigo").value("VEICULO_NAO_PERTENCE_AO_CLIENTE"));
        post("/ordens-servico", mecanico, corpo.formatted(999999, veiculoId)).andExpect(status().isNotFound());
        post("/ordens-servico", mecanico, corpo.formatted(cliente.id(), 999999)).andExpect(status().isNotFound());
    }

    @Test
    void permissoesDoClienteNaOrdem() throws Exception {
        String os = aguardandoAprovacao(1);
        ClienteCriado outro = novoCliente();

        get(os, outro.token()).andExpect(status().isNotFound());
        post(os + "/aprovacao", outro.token(), """
                {"aprovado": true}""").andExpect(status().isNotFound());
        post(os + "/aprovacao", mecanico, """
                {"aprovado": true}""").andExpect(status().isForbidden());
        post("/ordens-servico", cliente.token(), """
                {"clienteId": %d, "veiculoId": %d}""".formatted(cliente.id(), veiculoId))
                .andExpect(status().isForbidden());
        post(os + "/diagnostico/inicio", cliente.token(), null).andExpect(status().isForbidden());
        get("/ordens-servico", outro.token())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
        get("/ordens-servico?clienteId=" + cliente.id(), cliente.token())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].clienteId", everyItem(is(cliente.id()))));
        get("/ordens-servico?status=AGUARDANDO_APROVACAO&veiculoId=" + veiculoId, admin)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void tempoMedioDeExecucaoConsideraSoAsOrdensExecutadas() throws Exception {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        String metrica = "/metricas/tempo-medio-execucao?dataInicio=%s&dataFim=%s"
                .formatted(hoje.minusDays(1), hoje.plusDays(1));
        int antes = ordensConsideradas(metrica);

        String rejeitada = aguardandoAprovacao(1);
        post(rejeitada + "/aprovacao", admin, """
                {"aprovado": false}""").andExpect(status().isOk());
        assertThat(ordensConsideradas(metrica)).isEqualTo(antes);

        String executada = aguardandoAprovacao(1);
        post(executada + "/aprovacao", admin, """
                {"aprovado": true}""").andExpect(status().isOk());
        post(executada + "/finalizacao", mecanico, null).andExpect(status().isOk());
        get(metrica, admin)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.periodoInicio").value(hoje.minusDays(1).toString()))
                .andExpect(jsonPath("$.periodoFim").value(hoje.plusDays(1).toString()))
                .andExpect(jsonPath("$.totalOrdensConsideradas").value(antes + 1))
                .andExpect(jsonPath("$.tempoMedioExecucaoMinutos").isNumber());

        get(metrica, mecanico).andExpect(status().isForbidden());
        get("/metricas/tempo-medio-execucao?dataInicio=2024-01-01&dataFim=2026-01-01", admin)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACAO"));
    }

    @Test
    void registrosUsadosEmOrdemNaoSaoRemovidos() throws Exception {
        abrir("");

        delete("/servicos/" + servicoId, admin)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("REGISTRO_EM_USO"));
        delete("/veiculos/" + veiculoId, admin).andExpect(status().isConflict());
    }

    private String abrir(String extras) throws Exception {
        ResultActions aberta = post("/ordens-servico", mecanico, """
                {"clienteId": %d, "veiculoId": %d, "servicos": [{"servicoId": %d}]%s}"""
                .formatted(cliente.id(), veiculoId, servicoId, extras)).andExpect(status().isCreated());
        return "/ordens-servico/" + ler(aberta, "$.id");
    }

    private String abrirSemItens() throws Exception {
        ResultActions aberta = post("/ordens-servico", mecanico, """
                {"clienteId": %d, "veiculoId": %d}""".formatted(cliente.id(), veiculoId))
                .andExpect(status().isCreated());
        return "/ordens-servico/" + ler(aberta, "$.id");
    }

    private String aguardandoAprovacao(int quantidadePecas) throws Exception {
        String os = abrir(", \"pecas\": [{\"pecaInsumoId\": %d, \"quantidade\": %d}]".formatted(pecaId,
                quantidadePecas));
        post(os + "/diagnostico/inicio", mecanico, null).andExpect(status().isOk());
        post(os + "/diagnostico", mecanico, """
                {"diagnosticoTecnico": "Laudo"}""").andExpect(status().isOk());
        return os;
    }

    private int ordensConsideradas(String metrica) throws Exception {
        return ler(get(metrica, admin).andExpect(status().isOk()), "$.totalOrdensConsideradas");
    }

    private ResultActions saldo(Integer peca) throws Exception {
        return get("/pecas/" + peca, mecanico).andExpect(status().isOk());
    }
}
