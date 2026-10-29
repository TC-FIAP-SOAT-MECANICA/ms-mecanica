package br.com.oficina.infrastructure.dataproviders.database.implementations.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.domain.model.cadastro.Cliente;
import br.com.oficina.domain.valueobject.Documento;
import br.com.oficina.infrastructure.dataproviders.database.crypto.IndiceCego;
import br.com.oficina.infrastructure.dataproviders.database.entities.cadastro.ClienteEntity;
import br.com.oficina.infrastructure.dataproviders.database.repositories.cadastro.ClienteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;
    private final IndiceCego indiceCego;

    ClienteRepositoryImpl(ClienteJpaRepository jpaRepository, IndiceCego indiceCego) {
        this.jpaRepository = jpaRepository;
        this.indiceCego = indiceCego;
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity salvo = jpaRepository.saveAndFlush(
                ClienteEntity.from(cliente, indiceCego.hash(cliente.documento().valor())));
        return cliente.comId(salvo.id());
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        return jpaRepository.findById(id).map(ClienteEntity::toDomain);
    }

    @Override
    public Optional<Cliente> findByDocumento(Documento documento) {
        return jpaRepository.findByCpfCnpjHash(indiceCego.hash(documento.valor())).map(ClienteEntity::toDomain);
    }

    @Override
    public Optional<Integer> findIdByUsuarioId(Integer usuarioId) {
        return jpaRepository.findIdByUsuarioId(usuarioId);
    }

    @Override
    public List<Cliente> findByUsuarioIds(Collection<Integer> usuarioIds) {
        return jpaRepository.findByUsuarioIdIn(usuarioIds).stream().map(ClienteEntity::toDomain).toList();
    }

    @Override
    public boolean existsByDocumento(Documento documento) {
        return jpaRepository.existsByCpfCnpjHash(indiceCego.hash(documento.valor()));
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaRepository.existsById(id);
    }
}
