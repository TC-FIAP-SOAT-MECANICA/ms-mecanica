package br.com.oficina.infrastructure.dataproviders.database.repositories.identidade;

import br.com.oficina.domain.model.identidade.Perfil;
import br.com.oficina.infrastructure.dataproviders.database.entities.identidade.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Integer>,
        JpaSpecificationExecutor<UsuarioEntity> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPerfil(Perfil perfil);
}
