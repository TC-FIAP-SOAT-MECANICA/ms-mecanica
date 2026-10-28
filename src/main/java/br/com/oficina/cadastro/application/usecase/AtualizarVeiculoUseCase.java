package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.dto.VeiculoInput;
import br.com.oficina.cadastro.application.dto.VeiculoOutput;
import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.domain.DadosVeiculo;
import br.com.oficina.cadastro.domain.Veiculo;
import br.com.oficina.shared.application.dto.Solicitante;
import br.com.oficina.shared.domain.exception.ForbiddenException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

import java.time.Clock;
import java.time.Year;

public class AtualizarVeiculoUseCase {

    private final VeiculoRepository repository;
    private final VeiculoValidator validator;
    private final Clock clock;

    public AtualizarVeiculoUseCase(VeiculoRepository repository, ClienteRepository clienteRepository, Clock clock) {
        this.repository = repository;
        this.validator = new VeiculoValidator(repository, clienteRepository);
        this.clock = clock;
    }

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
