package br.com.oficina.estoque.adapter.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface PecaInsumoJpaRepository extends JpaRepository<PecaInsumoJpaEntity, Integer>,
        JpaSpecificationExecutor<PecaInsumoJpaEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PecaInsumoJpaEntity p where p.id = :id")
    Optional<PecaInsumoJpaEntity> findByIdForUpdate(Integer id);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Integer id);
}
