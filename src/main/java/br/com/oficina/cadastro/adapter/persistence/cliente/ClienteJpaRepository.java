package br.com.oficina.cadastro.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositório do Spring Data. O Spring gera a implementação a partir do nome dos métodos
 * ({@code existsByDocumento} vira {@code select ... where documento = ?}).
 */
interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, UUID> {

    boolean existsByDocumento(String documento);
}
