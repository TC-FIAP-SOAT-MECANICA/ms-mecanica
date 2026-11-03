package br.com.oficina.infrastructure.dataproviders.database.repositories.catalogo;

import br.com.oficina.infrastructure.dataproviders.database.entities.catalogo.ServicoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoJpaRepository extends JpaRepository<ServicoEntity, Integer> {

    Page<ServicoEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
