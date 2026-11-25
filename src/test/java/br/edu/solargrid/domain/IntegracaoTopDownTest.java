package br.edu.solargrid.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegracaoTopDownTest {

    // Stubs utilizados no Passo 1 da Integracao Top-Down
    static class StubClienteConsumidor extends ClienteConsumidor {
        StubClienteConsumidor() { super("00000000000", "Cliente Stub", "RESIDENCIAL", 1); }
        @Override public double obterCustoDisponibilidadeMinimoKwh() { return 30.0; }
    }

    static class StubLeituraMedidor extends LeituraMedidor {
        StubLeituraMedidor() { super("STUB-MED", "11/2026", 100.0, 250.0, 50.0); }
        @Override public double calcularConsumoBrutoNoMes() { return 150.0; }
    }

    static class StubCreditoEnergia extends CreditoEnergia {
        StubCreditoEnergia() { super(null, 50.0, 12); }
        @Override public double abaterConsumo(double consumoKwh) { return 100.0; }
    }

    // Stub utilizado no Passo 4 da Integracao Top-Down
    static class StubInversor extends Inversor {
        StubInversor() { super("STUB-INV", "Fabricante Stub", 50.0, null); }
        @Override public int diagnosticarIndiceRiscoOperacional(double temp, double tensao, double efic, int horas) {
            return 80;
        }
    }

    // Stub utilizado no Passo 5 da Integracao Top-Down
    static class StubUsinaSolarFotovoltaica extends UsinaSolarFotovoltaica {
        StubUsinaSolarFotovoltaica() { super("STUB-US", "Municipio Stub", 100.0, 200, 5.0); }
        @Override public double calcularGeracaoEstimadaMensal() { return 12000.0; }
    }

    @Test
    @DisplayName("Integracao Passo 1 - Testa FaturaEnergia isolada com Stubs de Cliente, Leitura e Credito")
    void deveIntegrarFaturaEnergiaComStubs() {
        StubClienteConsumidor stubCliente = new StubClienteConsumidor();
        StubLeituraMedidor stubLeitura = new StubLeituraMedidor();
        StubCreditoEnergia stubCredito = new StubCreditoEnergia();

        FaturaEnergia fatura = new FaturaEnergia(1L, stubCliente, stubLeitura, stubCredito, 1, 0);
        double consumoAposStub = stubCredito.abaterConsumo(stubLeitura.calcularConsumoBrutoNoMes());
        double valorFinal = fatura.calcularValorFinalFatura(consumoAposStub, 0.0, 1, 0);

        assertEquals(30.0, stubCliente.obterCustoDisponibilidadeMinimoKwh());
        assertEquals(65.0, valorFinal, 0.01);
    }

    @Test
    @DisplayName("Integracao Passo 4 - Testa OrdemManutencao isolada com StubInversor")
    void deveIntegrarOrdemManutencaoComStubInversor() {
        StubInversor stubInversor = new StubInversor();
        int riscoSimulado = stubInversor.diagnosticarIndiceRiscoOperacional(0, 0, 0, 0);

        OrdemManutencao ordem = new OrdemManutencao("OS-INT-01", stubInversor, riscoSimulado);
        assertEquals("EMERGENCIAL", ordem.getPrioridade());
        assertEquals(1500.0, ordem.getCustoEstimado(), 0.01);
    }

    @Test
    @DisplayName("Integracao Passo 5 - Testa Inversor real com StubUsinaSolarFotovoltaica")
    void deveIntegrarInversorComStubUsina() {
        StubUsinaSolarFotovoltaica stubUsina = new StubUsinaSolarFotovoltaica();
        Inversor inversor = new Inversor("INV-INT-01", "WEG", 75.0, stubUsina);

        assertEquals(12000.0, inversor.getUsinaAlocada().calcularGeracaoEstimadaMensal(), 0.01);
        assertEquals(5, inversor.diagnosticarIndiceRiscoOperacional(30.0, 220.0, 95.0, 100));
    }
}
