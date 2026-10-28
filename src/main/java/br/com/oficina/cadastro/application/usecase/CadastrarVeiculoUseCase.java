package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.VeiculoInput;
import br.com.oficina.cadastro.application.dto.VeiculoOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.domain.DadosVeiculo;
import br.com.oficina.cadastro.domain.Veiculo;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ForbiddenException;

import java.time.Clock;
import java.time.Year;

public class CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;
    private final VeiculoValidator validator;
    private final Clock clock;

    public CadastrarVeiculoUseCase(VeiculoRepository repository, ClienteRepository clienteRepository, Clock clock) {
        this.repository = repository;
        this.validator = new VeiculoValidator(repository, clienteRepository);
        this.clock = clock;
    }

    public VeiculoOutput execute(VeiculoInput input, Solicitante solicitante) {
        if (!solicitante.podeVerCliente(input.clienteId())) {
            throw new ForbiddenException("Cliente só pode cadastrar veículos para si mesmo");
        }
        DadosVeiculo dados = input.toDados();
        validator.validar(dados, null);
        Veiculo veiculo = Veiculo.cadastrar(dados, Year.now(clock).getValue());
        return VeiculoOutput.from(repository.save(veiculo));
    }
}
