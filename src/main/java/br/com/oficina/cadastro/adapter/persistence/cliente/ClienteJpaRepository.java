package br.com.oficina.cadastro.adapter.persistence.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, Integer>,
        JpaSpecificationExecutor<ClienteJpaEntity> {

    @EntityGraph(attributePaths = "contato")
    Optional<ClienteJpaEntity> findWithContatoById(Integer id);

    @EntityGraph(attributePaths = "contato")
    Optional<ClienteJpaEntity> findByCpfCnpjHash(String cpfCnpjHash);

    boolean existsByCpfCnpjHash(String cpfCnpjHash);

    @Query("select c.id from ClienteJpaEntity c where c.usuarioId = :usuarioId")
    Optional<Integer> findIdByUsuarioId(Integer usuarioId);

    @Override
    @EntityGraph(attributePaths = "contato")
    Page<ClienteJpaEntity> findAll(Specification<ClienteJpaEntity> spec, Pageable pageable);
}
