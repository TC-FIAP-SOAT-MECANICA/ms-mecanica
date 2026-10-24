package br.com.oficina.cadastro.adapter.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "veiculo")
public class VeiculoJpaEntity {
    @Id
    private  UUID id;

    @Column(nullable = false, length = 150)
    private  String placa;

    @Column(nullable = false, length = 150)
    private String marca;

    @Column(nullable = false, length = 150)
    private  String modelo;

    @Column(length = 4)
    private  Integer ano;

    @Column(nullable = false, length = 150)
    private  String cor;

    @Column(nullable = false, length = 150)
    private  UUID clienteId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime criadoEm;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime atualizadoEm;
}
