package br.com.oficina.application.dto.atendimento;

import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.OrdemServicoPecaInsumo;
import br.com.oficina.domain.model.atendimento.OrdemServicoServico;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrdemServicoOutput(Integer id, Integer clienteId, Integer veiculoId, Integer mecanicoId, String status,
                                 String descricaoProblema, String diagnosticoTecnico, String valorTotal,
                                 Instant agendamentoDiagnostico, Instant agendamentoServico,
                                 String statusAprovacaoRejeicao, Instant dataAprovacaoRejeicao,
                                 Instant dataAbertura, Instant dataConclusao, List<Servico> servicos,
                                 List<Peca> pecas) {

    public record Servico(Integer id, Integer servicoId, String precoFinal) {

        static Servico from(OrdemServicoServico item) {
            return new Servico(item.id(), item.servicoId(), item.precoFinal().toString());
        }
    }

    public record Peca(Integer id, Integer pecaInsumoId, BigDecimal quantidade, String precoUnitario,
                       String subtotal) {

        static Peca from(OrdemServicoPecaInsumo item) {
            return new Peca(item.id(), item.pecaInsumoId(), item.quantidade().valor(),
                    item.precoUnitario().toString(), item.subtotal().toString());
        }
    }

    public static OrdemServicoOutput from(OrdemServico os) {
        return new OrdemServicoOutput(os.id(), os.clienteId(), os.veiculoId(), os.mecanicoId(), os.status().name(),
                os.descricaoProblema(), os.diagnosticoTecnico(), os.valorTotal().toString(),
                os.agendamentoDiagnostico(), os.agendamentoServico(),
                os.statusAprovacaoRejeicao() == null ? null : os.statusAprovacaoRejeicao().name(),
                os.dataAprovacaoRejeicao(), os.dataAbertura(), os.dataConclusao(),
                os.servicos().stream().map(Servico::from).toList(), os.pecas().stream().map(Peca::from).toList());
    }
}
