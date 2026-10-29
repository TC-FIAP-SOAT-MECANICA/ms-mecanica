package br.com.oficina.application.usecase.implementation.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoInput;
import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.AtualizarServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.catalogo.Servico;
import br.com.oficina.domain.valueobject.Dinheiro;

public class AtualizarServicoUseCaseImpl implements AtualizarServicoUseCase {

    private final ServicoRepository repository;

    public AtualizarServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput execute(Integer servicoId, ServicoInput input) {
        Servico servico = repository.findById(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", servicoId));
        servico.atualizar(input.nome(), input.descricao(), Dinheiro.de(input.precoBase()));
        return ServicoOutput.from(repository.save(servico));
    }
}
