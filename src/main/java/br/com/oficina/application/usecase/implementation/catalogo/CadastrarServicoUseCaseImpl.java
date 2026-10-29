package br.com.oficina.application.usecase.implementation.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoInput;
import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.CadastrarServicoUseCase;
import br.com.oficina.domain.model.catalogo.Servico;
import br.com.oficina.domain.valueobject.Dinheiro;

public class CadastrarServicoUseCaseImpl implements CadastrarServicoUseCase {

    private final ServicoRepository repository;

    public CadastrarServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput execute(ServicoInput input) {
        Servico servico = Servico.criar(input.nome(), input.descricao(), Dinheiro.de(input.precoBase()));
        return ServicoOutput.from(repository.save(servico));
    }
}
