package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface ObterClienteUseCase {

    ClienteOutput execute(Integer clienteId, Solicitante solicitante);

    ClienteOutput execute(Integer clienteId);
}
