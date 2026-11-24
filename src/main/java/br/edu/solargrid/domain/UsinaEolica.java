package br.edu.solargrid.domain;

public class UsinaEolica extends UnidadeGeradora {
    private int numeroAerogeradores;
    private double velocidadeMediaVentoMs;

    public UsinaEolica(String codigo, String municipio, double potenciaKw, int numeroAerogeradores, double velocidadeMediaVentoMs) {
        super(codigo, municipio, potenciaKw);
        this.numeroAerogeradores = numeroAerogeradores;
        this.velocidadeMediaVentoMs = velocidadeMediaVentoMs;
    }

    @Override
    public double calcularGeracaoEstimadaMensal() {
        return potenciaInstaladaKw * (velocidadeMediaVentoMs / 10.0) * 180.0;
    }

    public boolean verificarVelocidadeSegura() {
        return velocidadeMediaVentoMs >= 3.0 && velocidadeMediaVentoMs <= 25.0;
    }

    public int getNumeroAerogeradores() { return numeroAerogeradores; }
    public double getVelocidadeMediaVentoMs() { return velocidadeMediaVentoMs; }
}
