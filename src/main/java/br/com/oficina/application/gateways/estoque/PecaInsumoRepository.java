package br.com.oficina.application.gateways.estoque;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.domain.model.estoque.PecaInsumo;

import java.util.Optional;

public interface PecaInsumoRepository {

    PecaInsumo save(PecaInsumo pecaInsumo);

    Optional<PecaInsumo> findById(Integer id);

    Optional<PecaInsumo> findByIdForUpdate(Integer id);

    boolean existsBySku(String sku, Integer ignorarId);

    PageResult<PecaInsumo> findAll(String nome, boolean somenteAbaixoDoMinimo, PageQuery pageQuery);

    void deleteById(Integer id);
}
