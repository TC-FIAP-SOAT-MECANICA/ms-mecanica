package br.com.oficina.application.usecase.cadastro;

import br.com.oficina.application.dto.cadastro.ClienteOutput;

public interface PesquisarClientePorDocumentoUseCase {

    ClienteOutput execute(String documento);
}
