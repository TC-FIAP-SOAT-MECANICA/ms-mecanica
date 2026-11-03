package br.com.oficina.infrastructure.dataproviders.database.entities.cadastro;

import br.com.oficina.domain.model.cadastro.DadosVeiculo;
import br.com.oficina.domain.model.cadastro.Placa;
import br.com.oficina.domain.model.cadastro.Veiculo;
import br.com.oficina.infrastructure.dataproviders.database.crypto.CampoCriptografadoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculos")
public class VeiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Convert(converter = CampoCriptografadoConverter.class)
    @Column(nullable = false)
    private String placa;

    @Column(name = "placa_hash", unique = true, length = 64)
    private String placaHash;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    @Column(length = 30)
    private String cor;

    @Column(unique = true, length = 30)
    private String chassi;

    private Integer quilometragem;

    protected VeiculoEntity() {
    }

    public static VeiculoEntity from(Veiculo veiculo, String placaHash) {
        var entity = new VeiculoEntity();
        entity.id = veiculo.id();
        entity.clienteId = veiculo.clienteId();
        entity.placa = veiculo.placa().valor();
        entity.placaHash = placaHash;
        entity.marca = veiculo.marca();
        entity.modelo = veiculo.modelo();
        entity.ano = veiculo.ano();
        entity.cor = veiculo.cor();
        entity.chassi = veiculo.chassi();
        entity.quilometragem = veiculo.quilometragem();
        return entity;
    }

    public Veiculo toDomain() {
        return Veiculo.restaurar(id, new DadosVeiculo(clienteId, new Placa(placa), marca, modelo, ano, cor, chassi,
                quilometragem));
    }
}
