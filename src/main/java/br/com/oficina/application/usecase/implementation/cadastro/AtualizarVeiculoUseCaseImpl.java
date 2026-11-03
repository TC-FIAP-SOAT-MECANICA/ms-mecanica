package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.dto.cadastro.VeiculoInput;
import br.com.oficina.application.dto.cadastro.VeiculoOutput;
import br.com.oficina.application.dto.shared.Solicitante;
import br.com.oficina.application.exceptions.ForbiddenException;
import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.application.usecase.cadastro.AtualizarVeiculoUseCase;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.cadastro.DadosVeiculo;
import br.com.oficina.domain.model.cadastro.Veiculo;

import java.time.Clock;
import java.time.Year;

public class AtualizarVeiculoUseCaseImpl implements AtualizarVeiculoUseCase {

    private final VeiculoRepository repository;
    private final VeiculoValidator validator;
    private final Clock clock;

    public AtualizarVeiculoUseCaseImpl(VeiculoRepository repository, ClienteRepository clienteRepository, Clock clock) {
        this.repository = repository;
        this.validator = new VeiculoValidator(repository, clienteRepository);
        this.clock = clock;
    }

    @Override
    public VeiculoOutput execute(Integer veiculoId, VeiculoInput input, Solicitante solicitante) {
        Veiculo veiculo = repository.findById(veiculoId)
                .filter(encontrado -> solicitante.podeVerCliente(encontrado.clienteId()))
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));
        if (!solicitante.podeVerCliente(input.clienteId())) {
            throw new ForbiddenException("Cliente não pode transferir o veículo para outro cliente");
        }
        DadosVeiculo dados = input.toDados();
        validator.validar(dados, veiculoId);
        veiculo.atualizar(dados, Year.now(clock).getValue());
        return VeiculoOutput.from(repository.save(veiculo));
    }
}
