package br.com.oficina.catalogo.adapter.persistence;

import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.domain.Servico;
import org.springframework.stereotype.Component;

/**
 * Adapter: implementa o port {@link ServicoRepository} usando JPA.
 *
 * <p>Converte a entidade de domínio para a entidade JPA e delega ao Spring Data. Para trocar o banco,
 * basta escrever outro adapter — o caso de uso e o domínio não mudam.
 */
@Component
class ServicoRepositoryAdapter implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;

    ServicoRepositoryAdapter(ServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Servico servico) {
        jpaRepository.save(ServicoJpaEntity.from(servico));
    }
}
