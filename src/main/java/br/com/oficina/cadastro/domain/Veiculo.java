package br.com.oficina.cadastro.domain;

import java.util.Objects;

public class Veiculo {

    private static final int ANO_MINIMO = 1950;

    private final Integer id;
    private Integer clienteId;
    private Placa placa;
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private String chassi;
    private Integer quilometragem;

    private Veiculo(Integer id) {
        this.id = id;
    }

    public static Veiculo cadastrar(DadosVeiculo dados, int anoAtual) {
        var veiculo = new Veiculo(null);
        veiculo.atualizar(dados, anoAtual);
        return veiculo;
    }

    public static Veiculo restaurar(Integer id, DadosVeiculo dados) {
        var veiculo = new Veiculo(id);
        veiculo.aplicar(dados);
        return veiculo;
    }

    public void atualizar(DadosVeiculo dados, int anoAtual) {
        Objects.requireNonNull(dados.clienteId(), "clienteId");
        Objects.requireNonNull(dados.placa(), "placa");
        obrigatorio(dados.marca(), "Marca");
        obrigatorio(dados.modelo(), "Modelo");
        if (dados.ano() == null || dados.ano() < ANO_MINIMO || dados.ano() > anoAtual + 1) {
            throw new IllegalArgumentException("Ano deve estar entre " + ANO_MINIMO + " e " + (anoAtual + 1));
        }
        if (dados.quilometragem() != null && dados.quilometragem() < 0) {
            throw new IllegalArgumentException("Quilometragem não pode ser negativa");
        }
        aplicar(new DadosVeiculo(dados.clienteId(), dados.placa(), dados.marca().trim(), dados.modelo().trim(),
                dados.ano(), vazioParaNulo(dados.cor()), normalizarChassi(dados.chassi()), dados.quilometragem()));
    }

    public static String normalizarChassi(String chassi) {
        String valor = vazioParaNulo(chassi);
        return valor == null ? null : valor.toUpperCase();
    }

    private void aplicar(DadosVeiculo dados) {
        this.clienteId = dados.clienteId();
        this.placa = dados.placa();
        this.marca = dados.marca();
        this.modelo = dados.modelo();
        this.ano = dados.ano();
        this.cor = dados.cor();
        this.chassi = dados.chassi();
        this.quilometragem = dados.quilometragem();
    }

    private static void obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
    }

    private static String vazioParaNulo(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    public Integer id() {
        return id;
    }

    public Integer clienteId() {
        return clienteId;
    }

    public Placa placa() {
        return placa;
    }

    public String marca() {
        return marca;
    }

    public String modelo() {
        return modelo;
    }

    public int ano() {
        return ano;
    }

    public String cor() {
        return cor;
    }

    public String chassi() {
        return chassi;
    }

    public Integer quilometragem() {
        return quilometragem;
    }
}
