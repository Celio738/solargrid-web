package br.edu.solargrid.domain;

public class ClienteConsumidor {
    private String documentoCpfCnpj;
    private String nomeRazaoSocial;
    private String classeConsumo;
    private int faseLigacao;

    public ClienteConsumidor(String documentoCpfCnpj, String nomeRazaoSocial, String classeConsumo, int faseLigacao) {
        this.documentoCpfCnpj = documentoCpfCnpj;
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.classeConsumo = classeConsumo;
        this.faseLigacao = faseLigacao;
    }

    public double obterCustoDisponibilidadeMinimoKwh() {
        if (faseLigacao == 1) return 30.0;
        if (faseLigacao == 2) return 50.0;
        return 100.0;
    }

    public String getDocumentoCpfCnpj() { return documentoCpfCnpj; }
    public String getNomeRazaoSocial() { return nomeRazaoSocial; }
    public String getClasseConsumo() { return classeConsumo; }
    public int getFaseLigacao() { return faseLigacao; }
}
