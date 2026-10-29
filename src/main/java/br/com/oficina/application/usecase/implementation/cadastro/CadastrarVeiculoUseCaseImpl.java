package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoInput;
import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.exceptions.ForbiddenException;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.CadastrarVeiculoUseCase;
import br.com.oficina.domain.model.cadastro.DadosVeiculo;
import br.com.oficina.domain.model.cadastro.Veiculo;

import java.time.Clock;
import java.time.Year;

public class CadastrarVeiculoUseCaseImpl implements CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;
    private final VeiculoValidator validator;
    private final Clock clock;

    public CadastrarVeiculoUseCaseImpl(VeiculoRepository repository, ClienteRepository clienteRepository, Clock clock) {
        this.repository = repository;
        this.validator = new VeiculoValidator(repository, clienteRepository);
        this.clock = clock;
    }

    @Override
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
