package br.com.oficina.application.usecase.implementation.catalogo;

import br.com.oficina.application.dto.catalogo.ServicoOutput;
import br.com.oficina.application.gateways.catalogo.ServicoRepository;
import br.com.oficina.application.usecase.catalogo.ObterServicoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;

public class ObterServicoUseCaseImpl implements ObterServicoUseCase {

    private final ServicoRepository repository;

    public ObterServicoUseCaseImpl(ServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServicoOutput execute(Integer servicoId) {
        return repository.findById(servicoId)
                .map(ServicoOutput::from)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", servicoId));
    }
}
