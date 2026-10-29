package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoInput;
import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface AtualizarVeiculoUseCase {

    VeiculoOutput execute(Integer veiculoId, VeiculoInput input, Solicitante solicitante);
}
