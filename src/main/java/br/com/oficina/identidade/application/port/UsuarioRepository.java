package br.com.oficina.identidade.application.port;

import br.com.oficina.identidade.domain.Perfil;
import br.com.oficina.identidade.domain.Usuario;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Integer id);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPerfil(Perfil perfil);

    PageResult<Usuario> findAll(Perfil perfil, PageQuery pageQuery);

    void deleteById(Integer id);
}
