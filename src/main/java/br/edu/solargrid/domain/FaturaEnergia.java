package br.edu.solargrid.domain;

public class FaturaEnergia {
    private Long id;
    private ClienteConsumidor cliente;
    private LeituraMedidor leitura;
    private CreditoEnergia credito;
    private int bandeiraTarifaria;
    private int diasAtraso;
    private double valorTotalCalculado;

    public FaturaEnergia(Long id, ClienteConsumidor cliente, LeituraMedidor leitura, CreditoEnergia credito, int bandeiraTarifaria, int diasAtraso) {
        this.id = id;
        this.cliente = cliente;
        this.leitura = leitura;
        this.credito = credito;
        this.bandeiraTarifaria = bandeiraTarifaria;
        this.diasAtraso = diasAtraso;
    }

    public double calcularValorFinalFatura(double consumoKwh, double creditoInjetadoKwh, int bandeira, int diasAtraso) {
        if (consumoKwh < 0.0) {
            throw new IllegalArgumentException("Consumo nao pode ser negativo");
        }
        if (creditoInjetadoKwh < 0.0) {
            throw new IllegalArgumentException("Credito nao pode ser negativo");
        }

        double consumoLiquido = consumoKwh - creditoInjetadoKwh;
        if (consumoLiquido < 30.0) {
            consumoLiquido = 30.0;
        }

        double tarifaBaseKwh;
        if (consumoLiquido <= 100.0) {
            tarifaBaseKwh = 0.65;
        } else if (consumoLiquido <= 300.0) {
            tarifaBaseKwh = 0.85;
        } else if (consumoLiquido <= 500.0) {
            tarifaBaseKwh = 0.95;
        } else {
            tarifaBaseKwh = 1.15;
        }

        double adicionalBandeiraPorKwh = 0.0;
        if (bandeira == 2) {
            adicionalBandeiraPorKwh = 0.02;
        } else if (bandeira == 3) {
            adicionalBandeiraPorKwh = 0.05;
        } else if (bandeira == 4) {
            adicionalBandeiraPorKwh = 0.08;
        }

        double subtotal = consumoLiquido * (tarifaBaseKwh + adicionalBandeiraPorKwh);

        if (diasAtraso > 0) {
            if (diasAtraso > 30) {
                subtotal = subtotal * 1.10;
            } else {
                subtotal = subtotal * 1.02;
            }
        }

        this.valorTotalCalculado = Math.round(subtotal * 100.0) / 100.0;
        return this.valorTotalCalculado;
    }

    public Long getId() { return id; }
    public ClienteConsumidor getCliente() { return cliente; }
    public LeituraMedidor getLeitura() { return leitura; }
    public CreditoEnergia getCredito() { return credito; }
    public int getBandeiraTarifaria() { return bandeiraTarifaria; }
    public int getDiasAtraso() { return diasAtraso; }
    public double getValorTotalCalculado() { return valorTotalCalculado; }
}
