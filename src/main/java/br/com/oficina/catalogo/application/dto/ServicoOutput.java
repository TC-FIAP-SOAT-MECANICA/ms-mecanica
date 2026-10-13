package br.com.oficina.catalogo.application.dto;

import br.com.oficina.catalogo.domain.Servico;

import java.util.UUID;

/**
 * Dados de saída do caso de uso. A entidade {@link Servico} nunca sai da camada de aplicação:
 * quem está do lado de fora recebe só esta cópia simples.
 */
public record ServicoOutput(UUID id, String nome, String descricao, String preco, int tempoEstimadoMinutos,
                            boolean ativo) {

    public static ServicoOutput from(Servico servico) {
        return new ServicoOutput(servico.id(), servico.nome(), servico.descricao(), servico.preco().toString(),
                servico.tempoEstimadoMinutos(), servico.ativo());
    }
}
