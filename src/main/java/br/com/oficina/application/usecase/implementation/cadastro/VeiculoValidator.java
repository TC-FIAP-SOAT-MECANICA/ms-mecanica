package br.com.oficina.application.usecase.implementation.cadastro;

import br.com.oficina.application.gateways.cadastro.ClienteRepository;
import br.com.oficina.application.gateways.cadastro.VeiculoRepository;
import br.com.oficina.domain.exceptions.ConflictException;
import br.com.oficina.domain.exceptions.ResourceNotFoundException;
import br.com.oficina.domain.model.cadastro.DadosVeiculo;
import br.com.oficina.domain.model.cadastro.Veiculo;

class VeiculoValidator {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    VeiculoValidator(VeiculoRepository veiculoRepository, ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    void validar(DadosVeiculo dados, Integer veiculoId) {
        if (!clienteRepository.existsById(dados.clienteId())) {
            throw new ResourceNotFoundException("Cliente", dados.clienteId());
        }
        if (veiculoRepository.existsByPlaca(dados.placa(), veiculoId)) {
            throw new ConflictException("PLACA_JA_CADASTRADA", "Já existe veículo com esta placa");
        }
        String chassi = Veiculo.normalizarChassi(dados.chassi());
        if (chassi != null && veiculoRepository.existsByChassi(chassi, veiculoId)) {
            throw new ConflictException("CHASSI_JA_CADASTRADO", "Já existe veículo com este chassi");
        }
    }
}
