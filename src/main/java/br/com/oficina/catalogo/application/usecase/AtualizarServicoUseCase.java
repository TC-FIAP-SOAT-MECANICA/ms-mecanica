package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.ServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import br.com.oficina.shared.domain.valueobject.Dinheiro;

public class AtualizarServicoUseCase {

    private final ServicoRepository repository;

    public AtualizarServicoUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoOutput execute(Integer servicoId, ServicoInput input) {
        Servico servico = repository.findById(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", servicoId));
        servico.atualizar(input.nome(), input.descricao(), Dinheiro.de(input.precoBase()));
        return ServicoOutput.from(repository.save(servico));
    }
}
