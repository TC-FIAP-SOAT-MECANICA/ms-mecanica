package br.com.oficina.catalogo.application.dto;

import br.com.oficina.catalogo.domain.Servico;

public record ServicoOutput(Integer id, String nome, String descricao, String precoBase) {

    public static ServicoOutput from(Servico servico) {
        return new ServicoOutput(servico.id(), servico.nome(), servico.descricao(), servico.precoBase().toString());
    }
}
