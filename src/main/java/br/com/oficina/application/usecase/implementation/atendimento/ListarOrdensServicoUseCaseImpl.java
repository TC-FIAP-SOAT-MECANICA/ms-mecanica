package br.com.oficina.application.usecase.implementation.atendimento;

import br.com.oficina.application.dto.atendimento.OrdemServicoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.atendimento.OrdemServicoRepository;
import br.com.oficina.application.usecase.atendimento.ListarOrdensServicoUseCase;
import br.com.oficina.domain.model.atendimento.StatusOrdemServico;

import java.util.List;

public class ListarOrdensServicoUseCaseImpl implements ListarOrdensServicoUseCase {

    private final OrdemServicoRepository repository;

    public ListarOrdensServicoUseCaseImpl(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<OrdemServicoOutput> execute(List<String> status, Integer clienteId, Integer veiculoId,
                                                  PageQuery pageQuery, Solicitante solicitante) {
        List<StatusOrdemServico> filtroStatus = status == null
                ? List.of()
                : status.stream().map(StatusOrdemServico::valueOf).toList();
        return repository.findAll(filtroStatus, solicitante.restringirClienteId(clienteId), veiculoId, pageQuery).map(OrdemServicoOutput::from);
    }
}
