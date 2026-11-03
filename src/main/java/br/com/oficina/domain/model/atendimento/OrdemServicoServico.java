package br.com.oficina.domain.model.atendimento;

import br.com.oficina.domain.valueobject.Dinheiro;

import java.util.Objects;

public record OrdemServicoServico(Integer id, Integer servicoId, Dinheiro precoFinal) {

    public OrdemServicoServico {
        Objects.requireNonNull(servicoId, "servicoId");
        Objects.requireNonNull(precoFinal, "precoFinal");
    }
}
