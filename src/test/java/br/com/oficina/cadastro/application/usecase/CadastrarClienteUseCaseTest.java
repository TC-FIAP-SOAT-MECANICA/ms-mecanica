package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.CadastrarClienteInput;
import br.com.oficina.cadastro.application.dto.ClienteOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.UsuarioGateway;
import br.com.oficina.cadastro.domain.Cliente;
import br.com.oficina.shared.application.dto.PageQuery;
import br.com.oficina.shared.application.dto.PageResult;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.application.port.UnitOfWork;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ForbiddenException;
import br.com.oficina.shared.domain.valueobject.Documento;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CadastrarClienteUseCaseTest {

    private static final Instant AGORA = Instant.parse("2026-10-25T12:00:00Z");

    private final InMemoryClienteRepository clientes = new InMemoryClienteRepository();
    private final FakeUsuarioGateway usuarios = new FakeUsuarioGateway();
    private final CadastrarClienteUseCase useCase = new CadastrarClienteUseCase(clientes, usuarios, new DirectUnitOfWork());

    @Test
    void criaUsuarioEClienteVinculados() {
        ClienteOutput cliente = useCase.execute(new CadastrarClienteInput("529.982.247-25", "João", "joao@email.com",
                "+5511999998888", "senhaForte1"), Solicitante.anonimo());

        assertThat(cliente.id()).isEqualTo(1);
        assertThat(cliente.usuarioId()).isEqualTo(100);
        assertThat(cliente.documento()).isEqualTo("52998224725");
        assertThat(cliente.tipoDocumento()).isEqualTo("CPF");
        assertThat(cliente.criadoEm()).isEqualTo(AGORA);
        assertThat(usuarios.criados).containsExactly("joao@email.com");
    }

    @Test
    void recusaDocumentoDuplicadoSemCriarUsuario() {
        useCase.execute(new CadastrarClienteInput("52998224725", "João", "joao@email.com", "+5511999998888",
                "senhaForte1"), Solicitante.anonimo());

        assertThatThrownBy(() -> useCase.execute(new CadastrarClienteInput("52998224725", "Outro",
                "outro@email.com", "+5511999998888", "senhaForte1"), Solicitante.anonimo()))
                .isInstanceOf(ConflictException.class)
                .extracting("code").isEqualTo("DOCUMENTO_JA_CADASTRADO");
        assertThat(usuarios.criados).hasSize(1);
    }

    @Test
    void clienteAutenticadoNaoCadastraOutroCliente() {
        Solicitante cliente = new Solicitante(5, Solicitante.Papel.CLIENTE, 1);

        assertThatThrownBy(() -> useCase.execute(new CadastrarClienteInput("52998224725", "João", "joao@email.com",
                "+5511999998888", "senhaForte1"), cliente))
                .isInstanceOf(ForbiddenException.class);
        assertThat(usuarios.criados).isEmpty();
    }

    @Test
    void recusaDocumentoInvalido() {
        assertThatThrownBy(() -> useCase.execute(new CadastrarClienteInput("52998224700", "João", "joao@email.com",
                "+5511999998888", "senhaForte1"), Solicitante.anonimo()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CPF/CNPJ inválido");
    }

    private static class DirectUnitOfWork implements UnitOfWork {

        @Override
        public <T> T execute(Supplier<T> work) {
            return work.get();
        }
    }

    private static class FakeUsuarioGateway implements UsuarioGateway {

        private final List<String> criados = new ArrayList<>();

        @Override
        public UsuarioCriado cadastrarUsuarioCliente(String nome, String email, String telefone, String senha) {
            criados.add(email);
            return new UsuarioCriado(99 + criados.size(), nome, email, telefone, AGORA);
        }

        @Override
        public ContatoUsuario atualizarContato(Integer usuarioId, String nome, String email, String telefone) {
            return new ContatoUsuario(nome, email, telefone);
        }

        @Override
        public void removerUsuario(Integer usuarioId) {
        }
    }

    private static class InMemoryClienteRepository implements ClienteRepository {

        private final List<Cliente> clientes = new ArrayList<>();

        @Override
        public Cliente save(Cliente cliente) {
            Cliente salvo = cliente.comId(clientes.size() + 1);
            clientes.add(salvo);
            return salvo;
        }

        @Override
        public Optional<Cliente> findById(Integer id) {
            return clientes.stream().filter(c -> c.id().equals(id)).findFirst();
        }

        @Override
        public Optional<Cliente> findByDocumento(Documento documento) {
            return clientes.stream().filter(c -> c.documento().equals(documento)).findFirst();
        }

        @Override
        public Optional<Integer> findIdByUsuarioId(Integer usuarioId) {
            return clientes.stream().filter(c -> c.usuarioId().equals(usuarioId)).map(Cliente::id).findFirst();
        }

        @Override
        public boolean existsByDocumento(Documento documento) {
            return findByDocumento(documento).isPresent();
        }

        @Override
        public boolean existsById(Integer id) {
            return findById(id).isPresent();
        }

        @Override
        public PageResult<Cliente> findAll(String nome, PageQuery pageQuery) {
            return new PageResult<>(List.copyOf(clientes), 0, clientes.size(), clientes.size(), 1);
        }
    }
}
