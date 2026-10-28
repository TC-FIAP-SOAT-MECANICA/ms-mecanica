package br.com.oficina.cadastro.application.usecase;

import br.com.oficina.cadastro.application.port.ClienteRepository;
import br.com.oficina.cadastro.application.port.VeiculoRepository;
import br.com.oficina.cadastro.domain.DadosVeiculo;
import br.com.oficina.cadastro.domain.Veiculo;
import br.com.oficina.shared.domain.exception.ConflictException;
import br.com.oficina.shared.domain.exception.ResourceNotFoundException;

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
