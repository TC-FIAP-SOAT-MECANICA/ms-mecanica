package br.com.oficina.application.dto.atendimento;

import java.time.Instant;
import java.util.List;

public record AbrirOrdemServicoInput(Integer clienteId, Integer veiculoId, String descricaoProblema,
                                     Instant agendamentoDiagnostico, List<ItemServicoInput> servicos,
                                     List<ItemPecaInput> pecas) {

    public AbrirOrdemServicoInput {
        servicos = servicos == null ? List.of() : List.copyOf(servicos);
        pecas = pecas == null ? List.of() : List.copyOf(pecas);
    }
}
