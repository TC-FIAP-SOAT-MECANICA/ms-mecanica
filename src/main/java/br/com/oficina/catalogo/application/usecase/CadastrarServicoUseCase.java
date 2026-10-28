package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.ServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.domain.valueobject.Dinheiro;

public class CadastrarServicoUseCase {

    private final ServicoRepository repository;

    public CadastrarServicoUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoOutput execute(ServicoInput input) {
        Servico servico = Servico.criar(input.nome(), input.descricao(), Dinheiro.de(input.precoBase()));
        return ServicoOutput.from(repository.save(servico));
    }
}
