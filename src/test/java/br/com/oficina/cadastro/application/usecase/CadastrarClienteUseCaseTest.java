package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;
import br.com.oficina.shared.domain.valueobject.Documento;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testa os casos de uso de cliente sem banco: o port {@link ClienteRepository} é implementado por um Map.
 * O relógio é fixo, então as datas são previsíveis.
 */
class CadastrarClienteUseCaseTest {

    private static final Instant AGORA = Instant.parse("2026-09-29T12:00:00Z");
    private static final String CPF_VALIDO = "52998224725";

    private final InMemoryClienteRepository repository = new InMemoryClienteRepository();
    private final CadastrarClienteUseCase cadastrar =
            new CadastrarClienteUseCase(repository, Clock.fixed(AGORA, ZoneOffset.UTC));
    private final ObterClienteUseCase obter = new ObterClienteUseCase(repository);

    @Test
    void cadastraEDepoisEncontraOCliente() {
        var criado = cadastrar.execute(new CadastrarClienteInput(CPF_VALIDO, "João da Silva", "JOAO@Email.com",
                null, null));

        var encontrado = obter.execute(criado.id());

        assertThat(encontrado.nome()).isEqualTo("João da Silva");
        assertThat(encontrado.email()).isEqualTo("joao@email.com");
        assertThat(encontrado.tipoDocumento()).isEqualTo("CPF");
        assertThat(encontrado.criadoEm()).isEqualTo(AGORA);
    }

    @Test
    void recusaDocumentoJaCadastrado() {
        cadastrar.execute(new CadastrarClienteInput(CPF_VALIDO, "João", "joao@email.com", null, null));

        assertThatThrownBy(() -> cadastrar.execute(
                new CadastrarClienteInput(CPF_VALIDO, "Outro", "outro@email.com", null, null)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void recusaCpfComDigitoVerificadorErrado() {
        assertThatThrownBy(() -> cadastrar.execute(
                new CadastrarClienteInput("52998224700", "João", "joao@email.com", null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CPF/CNPJ inválido");
        assertThat(repository.clientes).isEmpty();
    }

    @Test
    void obterClienteInexistenteLancaNaoEncontrado() {
        assertThatThrownBy(() -> obter.execute(UUID.randomUUID()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    /** Implementação do port em memória: faz o papel do banco no teste. */
    private static class InMemoryClienteRepository implements ClienteRepository {

        private final Map<UUID, Cliente> clientes = new HashMap<>();

        @Override
        public void save(Cliente cliente) {
            clientes.put(cliente.id(), cliente);
        }

        @Override
        public Optional<Cliente> findById(UUID id) {
            return Optional.ofNullable(clientes.get(id));
        }

        @Override
        public boolean existsByDocumento(Documento documento) {
            return clientes.values().stream().anyMatch(c -> c.documento().equals(documento));
        }
    }
}
