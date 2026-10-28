package br.com.oficina.atendimento.application.port;

import br.com.oficina.atendimento.domain.OrdemServico;
import br.com.oficina.atendimento.domain.StatusOrdemServico;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository {

    OrdemServico save(OrdemServico ordemServico);

    Optional<OrdemServico> findById(Integer id);

    PageResult<OrdemServico> findAll(List<StatusOrdemServico> status, Integer clienteId, Integer veiculoId,
                                     PageQuery pageQuery);
}
