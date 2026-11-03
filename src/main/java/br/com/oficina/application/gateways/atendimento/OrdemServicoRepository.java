package br.com.oficina.application.gateways.atendimento;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.domain.model.atendimento.OrdemServico;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository {

    OrdemServico save(OrdemServico ordemServico);

    Optional<OrdemServico> findById(Integer id);

    PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId, Integer veiculoId,
                                     PageQuery pageQuery);
}
