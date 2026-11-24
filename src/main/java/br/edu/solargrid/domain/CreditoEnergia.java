package br.edu.solargrid.domain;

public class CreditoEnergia {
    private ClienteConsumidor titular;
    private double saldoAcumuladoKwh;
    private int mesesParaExpirar;

    public CreditoEnergia(ClienteConsumidor titular, double saldoAcumuladoKwh, int mesesParaExpirar) {
        this.titular = titular;
        this.saldoAcumuladoKwh = saldoAcumuladoKwh;
        this.mesesParaExpirar = mesesParaExpirar;
    }

    public boolean possuiSaldoValido() {
        return saldoAcumuladoKwh > 0 && mesesParaExpirar > 0;
    }

    public double abaterConsumo(double consumoKwh) {
        if (!possuiSaldoValido()) return consumoKwh;
        if (saldoAcumuladoKwh >= consumoKwh) {
            saldoAcumuladoKwh -= consumoKwh;
            return 0.0;
        } else {
            double restante = consumoKwh - saldoAcumuladoKwh;
            saldoAcumuladoKwh = 0.0;
            return restante;
        }
    }

    public ClienteConsumidor getTitular() { return titular; }
    public double getSaldoAcumuladoKwh() { return saldoAcumuladoKwh; }
    public int getMesesParaExpirar() { return mesesParaExpirar; }
}
