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
    private StatusOrdemServico status;
    private final String descricaoProblema;
    private String diagnosticoTecnico;
    private Dinheiro valorTotal;
    private final Instant dataAbertura;
    private Instant dataConclusao;
    private final List<ItemServico> servicos;
    private final List<ItemPeca> pecas;

    private OrdemServico(Integer id, Integer clienteId, Integer veiculoId, StatusOrdemServico status,
                         String descricaoProblema, String diagnosticoTecnico, Instant dataAbertura,
                         Instant dataConclusao, List<ItemServico> servicos, List<ItemPeca> pecas) {
        this.id = id;
        this.clienteId = clienteId;
        this.veiculoId = veiculoId;
        this.status = status;
        this.descricaoProblema = descricaoProblema;
        this.diagnosticoTecnico = diagnosticoTecnico;
        this.dataAbertura = dataAbertura;
        this.dataConclusao = dataConclusao;
        this.servicos = new ArrayList<>(servicos);
        this.pecas = new ArrayList<>(pecas);
        recalcularValorTotal();
    }

    public static OrdemServico abrir(Integer clienteId, Integer veiculoId, String descricaoProblema, Instant agora) {
        return new OrdemServico(null, Objects.requireNonNull(clienteId, "clienteId"),
                Objects.requireNonNull(veiculoId, "veiculoId"), StatusOrdemServico.ABERTA, textoOuNulo(descricaoProblema),
                null, agora, null, List.of(), List.of());
    }

    public static OrdemServico restaurar(Integer id, Integer clienteId, Integer veiculoId, StatusOrdemServico status,
                                         String descricaoProblema, String diagnosticoTecnico, Instant dataAbertura,
                                         Instant dataConclusao, List<ItemServico> servicos, List<ItemPeca> pecas) {
        return new OrdemServico(id, clienteId, veiculoId, status, descricaoProblema, diagnosticoTecnico,
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
        servicos.add(new ItemServico(null, servicoId, precoFinal));
        recalcularValorTotal();
    }

    public ItemServico removerServico(Integer itemId) {
        garantirItensEditaveis();
        ItemServico item = servicos.stream()
                .filter(servico -> Objects.equals(servico.id(), itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Serviço da OS", itemId));
        servicos.remove(item);
        recalcularValorTotal();
        return item;
    }

    public void incluirPeca(Integer pecaInsumoId, Quantidade quantidade, Dinheiro precoUnitario) {
        garantirItensEditaveis();
        pecas.add(ItemPeca.novo(pecaInsumoId, quantidade, precoUnitario));
        recalcularValorTotal();
    }

    public ItemPeca removerPeca(Integer itemId) {
        garantirItensEditaveis();
        ItemPeca item = pecas.stream()
                .filter(peca -> Objects.equals(peca.id(), itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Peça da OS", itemId));
        pecas.remove(item);
        recalcularValorTotal();
        return item;
    }

    public void registrarDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new ValidationException("Diagnóstico técnico é obrigatório");
        }
        transicionar(StatusOrdemServico.ABERTA, StatusOrdemServico.EM_ANDAMENTO);
        this.diagnosticoTecnico = diagnostico.trim();
    }

    public void finalizar(Instant agora) {
        if (status == StatusOrdemServico.EM_ANDAMENTO && servicos.isEmpty()) {
            throw new BusinessRuleException("ORDEM_SEM_SERVICOS", "A OS precisa de ao menos um serviço para ser concluída");
        }
        transicionar(StatusOrdemServico.EM_ANDAMENTO, StatusOrdemServico.CONCLUIDA);
        this.dataConclusao = agora;
    }

    public void entregar() {
        transicionar(StatusOrdemServico.CONCLUIDA, StatusOrdemServico.ENTREGUE);
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
        for (ItemServico servico : servicos) {
            total = total.somar(servico.precoFinal());
        }
        for (ItemPeca peca : pecas) {
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

    public StatusOrdemServico status() {
        return status;
    }

    public String descricaoProblema() {
        return descricaoProblema;
    }

    public String diagnosticoTecnico() {
        return diagnosticoTecnico;
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

    public List<ItemServico> servicos() {
        return List.copyOf(servicos);
    }

    public List<ItemPeca> pecas() {
        return List.copyOf(pecas);
    }
}
