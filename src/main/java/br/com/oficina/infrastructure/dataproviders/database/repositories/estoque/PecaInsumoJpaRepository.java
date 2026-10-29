package br.com.oficina.infrastructure.dataproviders.database.repositories.estoque;

import br.com.oficina.infrastructure.dataproviders.database.entities.estoque.PecaInsumoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PecaInsumoJpaRepository extends JpaRepository<PecaInsumoEntity, Integer>,
        JpaSpecificationExecutor<PecaInsumoEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PecaInsumoEntity p where p.id = :id")
    Optional<PecaInsumoEntity> findByIdForUpdate(Integer id);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Integer id);
}
