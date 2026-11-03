package br.com.oficina.application.dto.catalogo;

import br.com.oficina.domain.model.catalogo.Servico;

public record ServicoOutput(Integer id, String nome, String descricao, String precoBase) {

    public static ServicoOutput from(Servico servico) {
        return new ServicoOutput(servico.id(), servico.nome(), servico.descricao(), servico.precoBase().toString());
    }
}
