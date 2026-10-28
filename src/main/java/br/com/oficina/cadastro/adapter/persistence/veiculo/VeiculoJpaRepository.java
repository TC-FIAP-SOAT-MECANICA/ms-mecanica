package br.com.oficina.cadastro.adapter.persistence.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface VeiculoJpaRepository extends JpaRepository<VeiculoJpaEntity, Integer>,
        JpaSpecificationExecutor<VeiculoJpaEntity> {

    boolean existsByPlacaHash(String placaHash);

    boolean existsByPlacaHashAndIdNot(String placaHash, Integer id);

    boolean existsByChassi(String chassi);

    boolean existsByChassiAndIdNot(String chassi, Integer id);
}
