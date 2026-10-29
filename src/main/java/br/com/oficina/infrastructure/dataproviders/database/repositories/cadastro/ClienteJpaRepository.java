package br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro;

import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Integer> {

    Optional<ClienteEntity> findByCpfCnpjHash(String cpfCnpjHash);

    boolean existsByCpfCnpjHash(String cpfCnpjHash);

    @Query("select c.id from ClienteEntity c where c.usuarioId = :usuarioId")
    Optional<Integer> findIdByUsuarioId(Integer usuarioId);

    List<ClienteEntity> findByUsuarioIdIn(Collection<Integer> usuarioIds);
}
