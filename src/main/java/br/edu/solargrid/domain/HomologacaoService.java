package br.edu.solargrid.domain;

public class HomologacaoService {
    private final ConcessionariaTarifaGateway gateway;

    public HomologacaoService(ConcessionariaTarifaGateway gateway) {
        this.gateway = gateway;
    }

    public double calcularCustoHomologacaoUsina(UnidadeGeradora usina) {
        boolean disponivel = gateway.verificarSubestacaoDisponivel(
                usina.getMunicipio(), usina.getPotenciaInstaladaKw()
        );
        if (!disponivel) {
            throw new IllegalStateException("Subestacao da concessionaria sem capacidade disponivel");
        }
        double aliquota = gateway.consultarAliquotaImpostoEstadual(usina.getMunicipio());
        return usina.calcularTaxaManutencaoBase() * (1.0 + aliquota);
    }
}
