package br.com.oficina.atendimento.domain;

import br.com.oficina.shared.domain.valueobject.Dinheiro;

import java.util.Objects;

public record ItemServico(Integer id, Integer servicoId, Dinheiro precoFinal) {

    public ItemServico {
        Objects.requireNonNull(servicoId, "servicoId");
        Objects.requireNonNull(precoFinal, "precoFinal");
    }
}
