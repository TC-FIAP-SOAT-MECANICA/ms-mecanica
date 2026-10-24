package br.com.oficina.catalogo.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/** Repositório do Spring Data. É um detalhe interno do adapter: o resto do sistema não o enxerga. */
interface ServicoJpaRepository extends JpaRepository<ServicoJpaEntity, UUID> {
}
