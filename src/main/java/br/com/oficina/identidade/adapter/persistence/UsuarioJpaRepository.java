package br.com.oficina.identidade.adapter.persistence;

import br.com.oficina.identidade.domain.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Integer> {

    Optional<UsuarioJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPerfil(Perfil perfil);

    Page<UsuarioJpaEntity> findByPerfil(Perfil perfil, Pageable pageable);
}
