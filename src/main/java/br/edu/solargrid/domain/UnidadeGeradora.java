package br.edu.solargrid.domain;

public abstract class UnidadeGeradora {
    protected String codigoIdentificacao;
    protected String municipio;
    protected double potenciaInstaladaKw;
    protected boolean ativa;

    public UnidadeGeradora(String codigoIdentificacao, String municipio, double potenciaInstaladaKw) {
        this.codigoIdentificacao = codigoIdentificacao;
        this.municipio = municipio;
        this.potenciaInstaladaKw = potenciaInstaladaKw;
        this.ativa = true;
    }

    public double calcularGeracaoEstimadaMensal() {
        return potenciaInstaladaKw * 120.0;
    }

    public double calcularTaxaManutencaoBase() {
        return potenciaInstaladaKw * 5.0;
    }

    public String getCodigoIdentificacao() { return codigoIdentificacao; }
    public String getMunicipio() { return municipio; }
    public double getPotenciaInstaladaKw() { return potenciaInstaladaKw; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}
