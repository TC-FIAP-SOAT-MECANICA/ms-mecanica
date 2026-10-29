package br.com.oficina.domain.model.atendimento;

public enum StatusOrdemServico {
    ABERTA,
    EM_ANDAMENTO,
    CONCLUIDA,
    ENTREGUE;

    public boolean permiteEditarItens() {
        return this == ABERTA || this == EM_ANDAMENTO;
    }
}
