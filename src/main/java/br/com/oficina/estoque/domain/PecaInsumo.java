package br.com.oficina.estoque.domain;

import br.com.oficina.shared.domain.exception.BusinessRuleException;
import br.com.oficina.shared.domain.valueobject.Dinheiro;
import br.com.oficina.shared.domain.valueobject.Quantidade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Pattern;

public class PecaInsumo {

    private static final Pattern UNIDADE = Pattern.compile("^[A-Z]{1,10}$");

    private final Integer id;
    private String sku;
    private String nome;
    private String unidadeMedida;
    private Dinheiro precoCusto;
    private Dinheiro precoVenda;
    private BigDecimal quantidadeEstoque;
    private BigDecimal estoqueMinimo;

    private PecaInsumo(Integer id, BigDecimal quantidadeEstoque) {
        this.id = id;
        this.quantidadeEstoque = escala(quantidadeEstoque);
    }

    public static PecaInsumo cadastrar(DadosPecaInsumo dados) {
        var peca = new PecaInsumo(null, BigDecimal.ZERO);
        peca.atualizarCadastro(dados);
        return peca;
    }

    public static PecaInsumo restaurar(Integer id, DadosPecaInsumo dados, BigDecimal quantidadeEstoque) {
        var peca = new PecaInsumo(id, quantidadeEstoque);
        peca.aplicar(dados);
        return peca;
    }

    public void atualizarCadastro(DadosPecaInsumo dados) {
        if (dados.sku() == null || dados.sku().isBlank()) {
            throw new IllegalArgumentException("SKU é obrigatório");
        }
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw new IllegalArgumentException("Nome da peça é obrigatório");
        }
        String unidade = dados.unidadeMedida() == null ? "" : dados.unidadeMedida().trim().toUpperCase();
        if (!UNIDADE.matcher(unidade).matches()) {
            throw new IllegalArgumentException("Unidade de medida inválida (ex.: UN, LT, KG)");
        }
        if (dados.estoqueMinimo() == null || dados.estoqueMinimo().signum() < 0) {
            throw new IllegalArgumentException("Estoque mínimo deve ser zero ou positivo");
        }
        aplicar(new DadosPecaInsumo(dados.sku().trim(), dados.nome().trim(), unidade,
                Objects.requireNonNull(dados.precoCusto(), "precoCusto"),
                Objects.requireNonNull(dados.precoVenda(), "precoVenda"), dados.estoqueMinimo()));
    }

    public void darEntrada(Quantidade quantidade) {
        quantidadeEstoque = quantidadeEstoque.add(quantidade.valor());
    }

    public void darSaida(Quantidade quantidade) {
        if (quantidadeEstoque.compareTo(quantidade.valor()) < 0) {
            throw new BusinessRuleException("ESTOQUE_INSUFICIENTE", "Estoque insuficiente para " + nome
                    + ": disponível " + quantidadeEstoque.toPlainString() + ", solicitado " + quantidade);
        }
        quantidadeEstoque = quantidadeEstoque.subtract(quantidade.valor());
    }

    public void ajustarInventario(BigDecimal saldoContado) {
        if (saldoContado == null || saldoContado.signum() < 0) {
            throw new IllegalArgumentException("Saldo contado deve ser zero ou positivo");
        }
        quantidadeEstoque = escala(saldoContado);
    }

    public boolean abaixoDoMinimo() {
        return quantidadeEstoque.compareTo(estoqueMinimo) < 0;
    }

    private void aplicar(DadosPecaInsumo dados) {
        this.sku = dados.sku();
        this.nome = dados.nome();
        this.unidadeMedida = dados.unidadeMedida();
        this.precoCusto = dados.precoCusto();
        this.precoVenda = dados.precoVenda();
        this.estoqueMinimo = escala(dados.estoqueMinimo());
    }

    private static BigDecimal escala(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Integer id() {
        return id;
    }

    public String sku() {
        return sku;
    }

    public String nome() {
        return nome;
    }

    public String unidadeMedida() {
        return unidadeMedida;
    }

    public Dinheiro precoCusto() {
        return precoCusto;
    }

    public Dinheiro precoVenda() {
        return precoVenda;
    }

    public BigDecimal quantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal estoqueMinimo() {
        return estoqueMinimo;
    }
}
