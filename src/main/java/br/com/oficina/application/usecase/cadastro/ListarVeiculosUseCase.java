package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.dto.shared.Solicitante;

public interface ListarVeiculosUseCase {

    PageResult<VeiculoOutput> execute(String placa, Integer clienteId, PageQuery pageQuery, Solicitante solicitante);
}
