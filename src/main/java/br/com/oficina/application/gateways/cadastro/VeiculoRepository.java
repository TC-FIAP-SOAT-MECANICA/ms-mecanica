package br.com.oficina.application.gateways.cadastro;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.domain.model.cadastro.Placa;
import br.com.oficina.domain.model.cadastro.Veiculo;

import java.util.Optional;

public interface VeiculoRepository {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(Integer id);

    boolean existsByPlaca(Placa placa, Integer ignorarId);

    boolean existsByChassi(String chassi, Integer ignorarId);

    PageResult<Veiculo> findAll(Placa placa, Integer clienteId, PageQuery pageQuery);

    void deleteById(Integer id);
}
