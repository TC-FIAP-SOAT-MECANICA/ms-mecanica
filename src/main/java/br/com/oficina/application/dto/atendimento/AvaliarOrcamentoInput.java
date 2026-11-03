package br.com.oficina.application.dto.atendimento;

import java.time.Instant;
import java.util.Objects;

public record AvaliarOrcamentoInput(Integer ordemServicoId, Boolean aprovado, Instant agendamentoServico) {

    public AvaliarOrcamentoInput {
        Objects.requireNonNull(aprovado, "aprovado");
    }
}
