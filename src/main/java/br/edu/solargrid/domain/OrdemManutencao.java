package br.edu.solargrid.domain;

public class OrdemManutencao {
    private String protocolo;
    private Inversor inversorAlvo;
    private int scoreRiscoDetectado;
    private String prioridade;
    private double custoEstimado;

    public OrdemManutencao(String protocolo, Inversor inversorAlvo, int scoreRiscoDetectado) {
        this.protocolo = protocolo;
        this.inversorAlvo = inversorAlvo;
        this.scoreRiscoDetectado = scoreRiscoDetectado;
        definirPrioridadeECusto();
    }

    private void definirPrioridadeECusto() {
        if (scoreRiscoDetectado >= 75) {
            this.prioridade = "EMERGENCIAL";
            this.custoEstimado = 1500.0;
        } else if (scoreRiscoDetectado >= 40) {
            this.prioridade = "ALTA";
            this.custoEstimado = 650.0;
        } else {
            this.prioridade = "PREVENTIVA";
            this.custoEstimado = 250.0;
        }
    }

    public String getProtocolo() { return protocolo; }
    public Inversor getInversorAlvo() { return inversorAlvo; }
    public String getPrioridade() { return prioridade; }
    public double getCustoEstimado() { return custoEstimado; }
}
