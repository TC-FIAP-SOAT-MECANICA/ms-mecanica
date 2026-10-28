package br.com.oficina.estoque.application.port;

import br.com.oficina.estoque.domain.PecaInsumo;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

import java.util.Optional;

public interface PecaInsumoRepository {

    PecaInsumo save(PecaInsumo pecaInsumo);

    Optional<PecaInsumo> findById(Integer id);

    Optional<PecaInsumo> findByIdForUpdate(Integer id);

    boolean existsBySku(String sku, Integer ignorarId);

    PageResult<PecaInsumo> findAll(String nome, boolean somenteAbaixoDoMinimo, PageQuery pageQuery);

    void deleteById(Integer id);
}
