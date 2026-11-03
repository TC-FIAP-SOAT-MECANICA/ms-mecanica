package br.com.oficina.application.usecase.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.dto.shared.Solicitante;

import java.util.List;

public interface ListarOrdensServicoUseCase {

    PageResult<OrdemServicoOutput> execute(List<String> status, Integer clienteId, Integer veiculoId,
                                           PageQuery pageQuery, Solicitante solicitante);
}
