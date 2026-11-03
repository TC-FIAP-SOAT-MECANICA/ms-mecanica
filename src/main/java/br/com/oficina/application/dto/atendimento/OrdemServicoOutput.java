package br.com.oficina.application.dto.atendimento;

import br.com.oficina.domain.model.atendimento.ItemPeca;
import br.com.oficina.domain.model.atendimento.ItemServico;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.catalogo.Servico;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrdemServicoOutput(Integer id, Integer clienteId, Integer veiculoId, String status,
                                 String descricaoProblema, String diagnosticoTecnico, String valorTotal,
                                 Instant dataAbertura, Instant dataConclusao, List<Servico> servicos,
                                 List<Peca> pecas) {

    public record Servico(Integer id, Integer servicoId, String precoFinal) {

        static Servico from(ItemServico item) {
            return new Servico(item.id(), item.servicoId(), item.precoFinal().toString());
        }
    }

    public record Peca(Integer id, Integer pecaInsumoId, BigDecimal quantidade, String precoUnitario,
                       String subtotal) {

        static Peca from(ItemPeca item) {
            return new Peca(item.id(), item.pecaInsumoId(), item.quantidade().valor(),
                    item.precoUnitario().toString(), item.subtotal().toString());
        }
    }

    public static OrdemServicoOutput from(OrdemServico os) {
        return new OrdemServicoOutput(os.id(), os.clienteId(), os.veiculoId(), os.status().name(),
                os.descricaoProblema(), os.diagnosticoTecnico(), os.valorTotal().toString(), os.dataAbertura(),
                os.dataConclusao(), os.servicos().stream().map(Servico::from).toList(),
                os.pecas().stream().map(Peca::from).toList());
    }
}
