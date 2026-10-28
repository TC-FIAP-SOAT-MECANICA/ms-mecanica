package br.com.oficina.cadastro.application.port;

import br.com.oficina.cadastro.domain.Placa;
import br.com.oficina.cadastro.domain.Veiculo;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

import java.util.Optional;

public interface VeiculoRepository {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(Integer id);

    boolean existsByPlaca(Placa placa, Integer ignorarId);

    boolean existsByChassi(String chassi, Integer ignorarId);

    PageResult<Veiculo> findAll(Placa placa, Integer clienteId, PageQuery pageQuery);

    void deleteById(Integer id);
}
