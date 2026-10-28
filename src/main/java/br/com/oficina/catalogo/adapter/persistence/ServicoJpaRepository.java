package br.com.oficina.catalogo.adapter.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ServicoJpaRepository extends JpaRepository<ServicoJpaEntity, Integer> {

    Page<ServicoJpaEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
