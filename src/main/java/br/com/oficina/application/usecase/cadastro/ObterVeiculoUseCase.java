package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface ObterVeiculoUseCase {

    VeiculoOutput execute(Integer veiculoId, Solicitante solicitante);

    VeiculoOutput execute(Integer veiculoId);
}
