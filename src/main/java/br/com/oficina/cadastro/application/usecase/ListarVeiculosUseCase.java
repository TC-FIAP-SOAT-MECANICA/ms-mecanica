package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.VeiculoOutput;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.domain.Placa;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import br.com.oficina.shared.application.dto.Solicitante;

public class ListarVeiculosUseCase {

    private final VeiculoRepository repository;

    public ListarVeiculosUseCase(VeiculoRepository repository) {
        this.repository = repository;
    }

    public PageResult<VeiculoOutput> execute(String placa, Integer clienteId, PageQuery pageQuery,
                                             Solicitante solicitante) {
        Placa filtroPlaca = placa == null || placa.isBlank() ? null : new Placa(placa);
        return repository.findAll(filtroPlaca, solicitante.restringirClienteId(clienteId), pageQuery).map(VeiculoOutput::from);
    }
}
