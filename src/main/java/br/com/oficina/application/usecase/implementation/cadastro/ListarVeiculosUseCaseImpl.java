package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.ListarVeiculosUseCase;
import br.com.oficina.domain.model.cadastro.Placa;

public class ListarVeiculosUseCaseImpl implements ListarVeiculosUseCase {

    private final VeiculoRepository repository;

    public ListarVeiculosUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<VeiculoOutput> execute(String placa, Integer clienteId, PageQuery pageQuery,
                                             Solicitante solicitante) {
        Placa filtroPlaca = placa == null || placa.isBlank() ? null : new Placa(placa);
        return repository.findAll(filtroPlaca, solicitante.restringirClienteId(clienteId), pageQuery).map(VeiculoOutput::from);
    }
}
