package br.edu.solargrid.domain;

public interface ConcessionariaTarifaGateway {
    boolean verificarSubestacaoDisponivel(String municipio, double potenciaKw);
    double consultarAliquotaImpostoEstadual(String municipio);
}
