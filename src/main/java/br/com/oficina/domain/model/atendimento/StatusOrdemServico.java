package br.com.oficina.domain.model.atendimento;

public enum StatusOrdemServico {
    RECEBIDA,
    EM_DIAGNOSTICO,
    AGUARDANDO_APROVACAO,
    EM_EXECUCAO,
    FINALIZADA,
    ENTREGUE;

    public boolean permiteEditarItens() {
        return this == RECEBIDA || this == EM_DIAGNOSTICO || this == EM_EXECUCAO;
    }

    public boolean consomeEstoque() {
        return this == EM_EXECUCAO;
    }
}
