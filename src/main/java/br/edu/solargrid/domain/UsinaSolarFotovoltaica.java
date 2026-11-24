package br.edu.solargrid.domain;

public class UsinaSolarFotovoltaica extends UnidadeGeradora {
    protected int quantidadeModulos;
    protected double indiceIrradiacaoSolar;

    public UsinaSolarFotovoltaica(String codigo, String municipio, double potenciaKw, int quantidadeModulos, double indiceIrradiacaoSolar) {
        super(codigo, municipio, potenciaKw);
        this.quantidadeModulos = quantidadeModulos;
        this.indiceIrradiacaoSolar = indiceIrradiacaoSolar;
    }

    @Override
    public double calcularGeracaoEstimadaMensal() {
        return potenciaInstaladaKw * indiceIrradiacaoSolar * 30.0 * 0.80;
    }

    @Override
    public double calcularTaxaManutencaoBase() {
        return super.calcularTaxaManutencaoBase() + (quantidadeModulos * 1.50);
    }

    public double calcularAreaOcupadaM2() {
        return quantidadeModulos * 2.2;
    }

    public int getQuantidadeModulos() { return quantidadeModulos; }
    public double getIndiceIrradiacaoSolar() { return indiceIrradiacaoSolar; }
}
