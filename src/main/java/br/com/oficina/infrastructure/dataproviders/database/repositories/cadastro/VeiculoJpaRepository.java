package br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro;

import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, Integer>,
        JpaSpecificationExecutor<VeiculoEntity> {

    boolean existsByPlacaHash(String placaHash);

    boolean existsByPlacaHashAndIdNot(String placaHash, Integer id);

    boolean existsByChassi(String chassi);

    boolean existsByChassiAndIdNot(String chassi, Integer id);
}
