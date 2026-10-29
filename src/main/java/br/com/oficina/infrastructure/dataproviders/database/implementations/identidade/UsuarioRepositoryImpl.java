package br.com.oficina.infrastructure.dataproviders.database.implementations.identidade;

import br.com.oficina.application.dto.shared.PageQuery;
import br.com.oficina.application.dto.shared.PageResult;
import br.com.oficina.application.gateways.identidade.UsuarioRepository;
import br.com.oficina.domain.model.identidade.Perfil;
import br.com.oficina.domain.model.identidade.Usuario;
import br.com.oficina.infrastructure.dataproviders.database.entities.identidade.UsuarioEntity;
import br.com.oficina.infrastructure.dataproviders.database.mappers.PageMapper;
import br.com.oficina.infrastructure.dataproviders.database.repositories.identidade.UsuarioJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return jpaRepository.saveAndFlush(UsuarioEntity.from(usuario)).toDomain();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return jpaRepository.findById(id).map(UsuarioEntity::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UsuarioEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Integer id) {
        return jpaRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsByPerfil(Perfil perfil) {
        return jpaRepository.existsByPerfil(perfil);
    }

    @Override
    public PageResult<Usuario> findAll(Perfil perfil, String nome, PageQuery pageQuery) {
        Specification<UsuarioEntity> filtro = (root, query, cb) -> cb.and(
                perfil == null ? cb.conjunction() : cb.equal(root.get("perfil"), perfil),
                nome == null || nome.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("nome")), "%" + nome.trim().toLowerCase() + "%"));
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("nome", "id"));
        return PageMapper.toResult(jpaRepository.findAll(filtro, pageable), UsuarioEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
