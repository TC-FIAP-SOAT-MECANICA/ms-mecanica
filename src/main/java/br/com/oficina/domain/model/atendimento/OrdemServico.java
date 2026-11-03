package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.exceptions.BusinessRuleException;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.exceptions.ValidationException;
import br.com.oficina.domain.valueobject.Dinheiro;
import br.com.oficina.domain.valueobject.Quantidade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdemServico {

    private final Integer id;
    private final Integer clienteId;
    private final Integer veiculoId;
    private Integer mecanicoId;
    private StatusOrdemServico status;
    private final String descricaoProblema;
    private String diagnosticoTecnico;
    private final Instant agendamentoDiagnostico;
    private Instant agendamentoServico;
    private StatusAprovacao statusAprovacaoRejeicao;
    private Instant dataAprovacaoRejeicao;
    private Dinheiro valorTotal;
    private final Instant dataAbertura;
    private Instant dataConclusao;
    private final List<OrdemServicoServico> servicos;
    private final List<OrdemServicoPecaInsumo> pecas;

    private OrdemServico(Integer id, Integer clienteId, Integer veiculoId, Integer mecanicoId,
                         StatusOrdemServico status, String descricaoProblema, String diagnosticoTecnico,
                         Instant agendamentoDiagnostico, Instant agendamentoServico,
                         StatusAprovacao statusAprovacaoRejeicao, Instant dataAprovacaoRejeicao, Instant dataAbertura,
                         Instant dataConclusao, List<OrdemServicoServico> servicos,
                         List<OrdemServicoPecaInsumo> pecas) {
        this.id = id;
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.mecanicoId = mecanicoId;
        this.status = status;
        this.descricaoProblema = descricaoProblema;
        this.diagnosticoTecnico = diagnosticoTecnico;
        this.agendamentoDiagnostico = agendamentoDiagnostico;
        this.agendamentoServico = agendamentoServico;
        this.statusAprovacaoRejeicao = statusAprovacaoRejeicao;
        this.dataAprovacaoRejeicao = dataAprovacaoRejeicao;
        this.dataAbertura = dataAbertura;
        this.dataConclusao = dataConclusao;
        this.servicos = new ArrayList<>(servicos);
        this.pecas = new ArrayList<>(pecas);
        recalcularValorTotal();
    }

    public static OrdemServico abrir(Integer clienteId, Integer veiculoId, String descricaoProblema,
                                     Instant agendamentoDiagnostico, Instant agora) {
        return new OrdemServico(null, Objects.requireNonNull(clienteId, "clienteId"),
                Objects.requireNonNull(veiculoId, "veiculoId"), null, StatusOrdemServico.RECEBIDA,
                textoOuNulo(descricaoProblema), null, agendamentoDiagnostico, null, null, null, agora, null,
                List.of(), List.of());
    }

    public static OrdemServico restaurar(Integer id, Integer clienteId, Integer veiculoId, Integer mecanicoId,
                                         StatusOrdemServico status, String descricaoProblema,
                                         String diagnosticoTecnico, Instant agendamentoDiagnostico,
                                         Instant agendamentoServico, StatusAprovacao statusAprovacaoRejeicao,
                                         Instant dataAprovacaoRejeicao, Instant dataAbertura, Instant dataConclusao,
                                         List<OrdemServicoServico> servicos, List<OrdemServicoPecaInsumo> pecas) {
        return new OrdemServico(id, clienteId, veiculoId, mecanicoId, status, descricaoProblema, diagnosticoTecnico,
                agendamentoDiagnostico, agendamentoServico, statusAprovacaoRejeicao, dataAprovacaoRejeicao,
                dataAbertura, dataConclusao, servicos, pecas);
    }

    public void garantirItensEditaveis() {
        if (!status.permiteEditarItens()) {
            throw new ConflictException("OS_ITEM_NAO_EDITAVEL",
                    "Itens não podem ser alterados em uma OS com status " + status);
        }
    }

    public void incluirServico(Integer servicoId, Dinheiro precoFinal) {
        garantirItensEditaveis();
        servicos.add(new OrdemServicoServico(null, servicoId, precoFinal));
        recalcularValorTotal();
    }

    public OrdemServicoServico removerServico(Integer itemId) {
        garantirItensEditaveis();
        OrdemServicoServico item = servicos.stream()
                .filter(servico -> Objects.equals(servico.id(), itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Serviço da OS", itemId));
        servicos.remove(item);
        recalcularValorTotal();
        return item;
    }

    public void incluirPeca(Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario) {
        garantirItensEditaveis();
        pecas.add(OrdemServicoPecaInsumo.novo(pecaInsumoId, quantidade, precoUnitario));
        recalcularValorTotal();
    }

    public OrdemServicoPecaInsumo removerPeca(Integer itemId) {
        garantirItensEditaveis();
        OrdemServicoPecaInsumo item = pecas.stream()
                .filter(peca -> Objects.equals(peca.id(), itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Peça da OS", itemId));
        pecas.remove(item);
        recalcularValorTotal();
        return item;
    }

    public void iniciarDiagnostico(Integer funcionarioId) {
        transicionar(StatusOrdemServico.RECEBIDA, StatusOrdemServico.EM_DIAGNOSTICO);
        this.mecanicoId = Objects.requireNonNull(funcionarioId, "funcionarioId");
    }

    public void registrarDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new ValidationException("Diagnóstico técnico é obrigatório");
        }
        if (status == StatusOrdemServico.EM_DIAGNOSTICO && servicos.isEmpty()) {
            throw new BusinessRuleException("ORDEM_SEM_SERVICOS",
                    "O orçamento precisa de ao menos um serviço para ser enviado ao cliente");
        }
        transicionar(StatusOrdemServico.EM_DIAGNOSTICO, StatusOrdemServico.AGUARDANDO_APROVACAO);
        this.diagnosticoTecnico = diagnostico.trim();
        this.statusAprovacaoRejeicao = StatusAprovacao.PENDENTE;
    }

    public void aprovarOrcamento(Instant agendamentoServico, Instant agora) {
        transicionar(StatusOrdemServico.AGUARDANDO_APROVACAO, StatusOrdemServico.EM_EXECUCAO);
        this.statusAprovacaoRejeicao = StatusAprovacao.APROVADO;
        this.dataAprovacaoRejeicao = agora;
        this.agendamentoServico = agendamentoServico;
    }

    public void rejeitarOrcamento(Instant agora) {
        transicionar(StatusOrdemServico.AGUARDANDO_APROVACAO, StatusOrdemServico.FINALIZADA);
        this.statusAprovacaoRejeicao = StatusAprovacao.REJEITADO;
        this.dataAprovacaoRejeicao = agora;
        this.dataConclusao = agora;
    }

    public void finalizar(Instant agora) {
        transicionar(StatusOrdemServico.EM_EXECUCAO, StatusOrdemServico.FINALIZADA);
        this.dataConclusao = agora;
    }

    public void entregar() {
        transicionar(StatusOrdemServico.FINALIZADA, StatusOrdemServico.ENTREGUE);
    }

    private void transicionar(StatusOrdemServico esperado, StatusOrdemServico novo) {
        if (status != esperado) {
            throw new ConflictException("OS_TRANSICAO_INVALIDA",
                    "Não é possível passar a OS de " + status + " para " + novo);
        }
        this.status = novo;
    }

    private void recalcularValorTotal() {
        Dinheiro total = Dinheiro.ZERO;
        for (OrdemServicoServico servico : servicos) {
            total = total.somar(servico.precoFinal());
        }
        for (OrdemServicoPecaInsumo peca : pecas) {
            total = total.somar(peca.subtotal());
        }
        this.valorTotal = total;
    }

    private static String textoOuNulo(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    public Integer id() {
        return id;
    }

    public Integer clienteId() {
        return clienteId;
    }

    public Integer veiculoId() {
        return veiculoId;
    }

    public Integer mecanicoId() {
        return mecanicoId;
    }

    public StatusOrdemServico status() {
        return status;
    }

    public String descricaoProblema() {
        return descricaoProblema;
    }

    public String diagnosticoTecnico() {
        return diagnosticoTecnico;
    }

    public Instant agendamentoDiagnostico() {
        return agendamentoDiagnostico;
    }

    public Instant agendamentoServico() {
        return agendamentoServico;
    }

    public StatusAprovacao statusAprovacaoRejeicao() {
        return statusAprovacaoRejeicao;
    }

    public Instant dataAprovacaoRejeicao() {
        return dataAprovacaoRejeicao;
    }

    public Dinheiro valorTotal() {
        return valorTotal;
    }

    public Instant dataAbertura() {
        return dataAbertura;
    }

    public Instant dataConclusao() {
        return dataConclusao;
    }

    public List<OrdemServicoServico> servicos() {
        return List.copyOf(servicos);
    }

    public List<OrdemServicoPecaInsumo> pecas() {
        return List.copyOf(pecas);
    }
}
