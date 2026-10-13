package br.com.oficina.catalogo.application.port;

import br.com.oficina.catalogo.domain.Servico;

/**
 * Port: o que o caso de uso precisa para guardar um serviço.
 *
 * <p>É só uma interface. Quem a implementa é {@code catalogo.adapter.persistence.ServicoRepositoryAdapter}.
 * Assim o caso de uso não sabe (nem precisa saber) se os dados vão para o Postgres, para a memória ou
 * para outro lugar.
 */
public interface ServicoRepository {

    void save(Servico servico);
}
