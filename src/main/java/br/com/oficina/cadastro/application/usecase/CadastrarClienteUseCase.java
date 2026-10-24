package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.valueobject.Documento;

import java.time.Clock;

/**
 * Caso de uso: cadastrar cliente ({@code POST /clientes}).
 *
 * <p>Regra de aplicação: o CPF/CNPJ não pode estar cadastrado em outro cliente. Essa checagem precisa
 * consultar o banco, por isso fica aqui (no caso de uso) e não na entidade.
 */
public class CadastrarClienteUseCase {

    private final ClienteRepository repository;
    private final Clock clock;

    public CadastrarClienteUseCase(ClienteRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public ClienteOutput execute(CadastrarClienteInput input) {
        Documento documento = new Documento(input.documento());
        if (repository.existsByDocumento(documento)) {
            throw new ConflictException("Já existe cliente com este CPF/CNPJ");
        }

        Cliente cliente = Cliente.cadastrar(documento, input.nome(), input.email(), input.telefone(),
                input.endereco() == null ? null : input.endereco().toDomain(), clock.instant());
        repository.save(cliente);
        return ClienteOutput.from(cliente);
    }
}
