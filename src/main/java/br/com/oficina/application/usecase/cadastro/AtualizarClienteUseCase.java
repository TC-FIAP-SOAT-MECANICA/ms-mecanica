package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.AtualizarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface AtualizarClienteUseCase {

    ClienteOutput execute(AtualizarClienteInput input, Solicitante solicitante);
}
