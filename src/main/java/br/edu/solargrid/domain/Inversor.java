package br.edu.solargrid.domain;

public class Inversor {
    private String numeroSerie;
    private String fabricante;
    private double potenciaNominalKw;
    private UsinaSolarFotovoltaica usinaAlocada;

    public Inversor(String numeroSerie, String fabricante, double potenciaNominalKw, UsinaSolarFotovoltaica usinaAlocada) {
        this.numeroSerie = numeroSerie;
        this.fabricante = fabricante;
        this.potenciaNominalKw = potenciaNominalKw;
        this.usinaAlocada = usinaAlocada;
    }

    public int diagnosticarIndiceRiscoOperacional(double temperaturaC, double tensaoV, double eficienciaPct, int horasContinuas) {
        if (temperaturaC < -10.0) {
            return 100;
        }
        if (temperaturaC > 85.0) {
            return 100;
        }

        int scoreRisco = 0;

        if (temperaturaC <= 45.0) {
            scoreRisco += 5;
        } else if (temperaturaC <= 65.0) {
            scoreRisco += 20;
        } else {
            scoreRisco += 40;
        }

        if (tensaoV < 180.0) {
            scoreRisco += 35;
        } else if (tensaoV > 260.0) {
            scoreRisco += 35;
        } else if (tensaoV < 200.0) {
            scoreRisco += 15;
        } else if (tensaoV > 240.0) {
            scoreRisco += 15;
        } else {
            scoreRisco += 0;
        }

        if (eficienciaPct < 75.0) {
            scoreRisco += 25;
        } else if (eficienciaPct < 90.0) {
            scoreRisco += 10;
        } else {
            scoreRisco += 0;
        }

        if (horasContinuas > 720) {
            scoreRisco += 10;
        }

        return scoreRisco;
    }

    public String getNumeroSerie() { return numeroSerie; }
    public String getFabricante() { return fabricante; }
    public double getPotenciaNominalKw() { return potenciaNominalKw; }
    public UsinaSolarFotovoltaica getUsinaAlocada() { return usinaAlocada; }
}
