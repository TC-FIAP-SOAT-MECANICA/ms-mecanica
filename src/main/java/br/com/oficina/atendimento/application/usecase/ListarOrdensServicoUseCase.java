package br.com.oficina.atendimento.application.usecase;

import br.com.oficina.atendimento.application.dto.OrdemServicoOutput;
import br.com.oficina.atendimento.application.port.OrdemServicoRepository;
import br.com.oficina.atendimento.domain.StatusOrdemServico;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import br.com.oficina.shared.application.dto.Solicitante;

import java.util.List;

public class ListarOrdensServicoUseCase {

    private final OrdemServicoRepository repository;

    public ListarOrdensServicoUseCase(OrdemServicoRepository repository) {
        this.repository = repository;
    }

    public PageResult<OrdemServicoOutput> execute(List<String> status, Integer clienteId, Integer veiculoId,
                                                  PageQuery pageQuery, Solicitante solicitante) {
        List<StatusOrdemServico> filtroStatus = status == null
                ? List.of()
                : status.stream().map(StatusOrdemServico::valueOf).toList();
        return repository.findAll(filtroStatus, solicitante.restringirClienteId(clienteId), veiculoId, pageQuery).map(OrdemServicoOutput::from);
    }
}
