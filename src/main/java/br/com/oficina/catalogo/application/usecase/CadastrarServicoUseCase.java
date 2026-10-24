package br.com.oficina.catalogo.application.usecase;

import br.com.oficina.catalogo.application.dto.CadastrarServicoInput;
import br.com.oficina.catalogo.application.dto.ServicoOutput;
import br.com.oficina.catalogo.application.port.ServicoRepository;
import br.com.oficina.catalogo.domain.Servico;
import br.com.oficina.shared.domain.valueobject.Dinheiro;

/**
 * Caso de uso: cadastrar um serviço no catálogo ({@code POST /servicos}).
 *
 * <p>Só orquestra: converte a entrada, pede ao domínio para criar o serviço (é lá que estão as regras)
 * e salva pelo port. Não tem anotação do Spring — quem cria esta classe é {@code CatalogoConfig}.
 */
public class CadastrarServicoUseCase {

    private final ServicoRepository repository;

    public CadastrarServicoUseCase(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoOutput execute(CadastrarServicoInput input) {
        Servico servico = Servico.criar(input.nome(), input.descricao(), Dinheiro.de(input.preco()),
                input.tempoEstimadoMinutos());
        repository.save(servico);
        return ServicoOutput.from(servico);
    }
}
