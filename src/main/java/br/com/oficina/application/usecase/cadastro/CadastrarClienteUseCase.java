package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.CadastrarClienteInput;
import br.com.oficina.application.dto.cadastro.ClienteOutput;
import br.com.oficina.application.dto.shared.Solicitante;

public interface CadastrarClienteUseCase {

    ClienteOutput execute(CadastrarClienteInput input);

    ClienteOutput execute(CadastrarClienteInput input, Solicitante solicitante);
}
