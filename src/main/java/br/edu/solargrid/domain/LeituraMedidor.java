package br.edu.solargrid.domain;

public class LeituraMedidor {
    private String codigoMedidor;
    private String mesReferencia;
    private double leituraAnteriorKwh;
    private double leituraAtualKwh;
    private double energiaInjetadaKwh;

    public LeituraMedidor(String codigoMedidor, String mesReferencia, double leituraAnteriorKwh, double leituraAtualKwh, double energiaInjetadaKwh) {
        this.codigoMedidor = codigoMedidor;
        this.mesReferencia = mesReferencia;
        this.leituraAnteriorKwh = leituraAnteriorKwh;
        this.leituraAtualKwh = leituraAtualKwh;
        this.energiaInjetadaKwh = energiaInjetadaKwh;
    }

    public double calcularConsumoBrutoNoMes() {
        if (leituraAtualKwh < leituraAnteriorKwh) {
            return 0.0;
        }
        return leituraAtualKwh - leituraAnteriorKwh;
    }

    public String getCodigoMedidor() { return codigoMedidor; }
    public String getMesReferencia() { return mesReferencia; }
    public double getLeituraAnteriorKwh() { return leituraAnteriorKwh; }
    public double getLeituraAtualKwh() { return leituraAtualKwh; }
    public double getEnergiaInjetadaKwh() { return energiaInjetadaKwh; }
}
