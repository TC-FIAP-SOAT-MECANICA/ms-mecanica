package br.com.oficina.identidade.adapter.persistence;

import br.com.oficina.identidade.application.port.UsuarioRepository;
import br.com.oficina.identidade.domain.Perfil;
import br.com.oficina.identidade.domain.Usuario;
import br.com.oficina.shared.adapter.persistence.PageMapper;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return jpaRepository.saveAndFlush(UsuarioJpaEntity.from(usuario)).toDomain();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return jpaRepository.findById(id).map(UsuarioJpaEntity::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UsuarioJpaEntity::toDomain);
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
    public PageResult<Usuario> findAll(Perfil perfil, PageQuery pageQuery) {
        var pageable = PageMapper.toPageable(pageQuery, Sort.by("nome", "id"));
        var page = perfil == null ? jpaRepository.findAll(pageable) : jpaRepository.findByPerfil(perfil, pageable);
        return PageMapper.toResult(page, UsuarioJpaEntity::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
        jpaRepository.flush();
    }
}
