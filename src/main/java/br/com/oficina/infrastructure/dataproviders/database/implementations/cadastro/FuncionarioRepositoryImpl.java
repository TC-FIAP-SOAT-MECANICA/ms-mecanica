package br.com.oficina.infrastructure.dataproviders.database.implementations.cadastro;

import br.com.oficina.application.gateways.cadastro.FuncionarioRepository;
import br.com.oficina.domain.model.cadastro.Funcionario;
import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.FuncionarioEntity;
import br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro.FuncionarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class FuncionarioRepositoryImpl implements FuncionarioRepository {

    private final FuncionarioJpaRepository jpaRepository;

    FuncionarioRepositoryImpl(FuncionarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Funcionario save(Funcionario funcionario) {
        return jpaRepository.saveAndFlush(FuncionarioEntity.from(funcionario)).toDomain();
    }

    @Override
    public Optional<Funcionario> findByUsuarioId(Integer usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).map(FuncionarioEntity::toDomain);
    }
}
