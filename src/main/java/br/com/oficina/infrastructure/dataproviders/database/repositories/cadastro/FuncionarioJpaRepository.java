package br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro;

import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioJpaRepository extends JpaRepository<FuncionarioEntity, Integer> {

    Optional<FuncionarioEntity> findByUsuarioId(Integer usuarioId);
}
