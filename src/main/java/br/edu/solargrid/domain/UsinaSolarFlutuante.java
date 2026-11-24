package br.edu.solargrid.domain;

public class UsinaSolarFlutuante extends UsinaSolarFotovoltaica {
    private String reservatorioHidrico;
    private double bonusResfriamentoNatural;

    public UsinaSolarFlutuante(String codigo, String municipio, double potenciaKw, int modulos, double irradiacao, String reservatorio, double bonusResfriamento) {
        super(codigo, municipio, potenciaKw, modulos, irradiacao);
        this.reservatorioHidrico = reservatorio;
        this.bonusResfriamentoNatural = bonusResfriamento;
    }

    @Override
    public double calcularGeracaoEstimadaMensal() {
        return super.calcularGeracaoEstimadaMensal() * (1.0 + bonusResfriamentoNatural);
    }

    public String getReservatorioHidrico() { return reservatorioHidrico; }
    public double getBonusResfriamentoNatural() { return bonusResfriamentoNatural; }
}
