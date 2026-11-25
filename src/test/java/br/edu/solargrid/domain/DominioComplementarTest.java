package br.edu.solargrid.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DominioComplementarTest {

    @Test
    @DisplayName("UnidadeGeradora - Deve calcular geracao base, taxa e alterar status ativo")
    void deveTestarUnidadeGeradoraBase() {
        UnidadeGeradora base = new UnidadeGeradora("UG-00", "Juiz de Fora", 10.0) {};

        assertEquals(1200.0, base.calcularGeracaoEstimadaMensal(), 0.01);
        assertEquals(50.0, base.calcularTaxaManutencaoBase(), 0.01);
        assertEquals("UG-00", base.getCodigoIdentificacao());
        assertEquals("Juiz de Fora", base.getMunicipio());
        assertEquals(10.0, base.getPotenciaInstaladaKw(), 0.01);
        assertTrue(base.isAtiva());

        base.setAtiva(false);
        assertFalse(base.isAtiva());
    }

    @Test
    @DisplayName("UsinaSolarFotovoltaica e UsinaSolarFlutuante - Deve validar polimorfismo e getters")
    void deveTestarHierarquiaSolar() {
        UsinaSolarFotovoltaica solar = new UsinaSolarFotovoltaica("US-01", "Juiz de Fora", 100.0, 200, 5.0);
        assertEquals(12000.0, solar.calcularGeracaoEstimadaMensal(), 0.01);
        assertEquals(800.0, solar.calcularTaxaManutencaoBase(), 0.01);
        assertEquals(440.0, solar.calcularAreaOcupadaM2(), 0.01);
        assertEquals(200, solar.getQuantidadeModulos());
        assertEquals(5.0, solar.getIndiceIrradiacaoSolar(), 0.01);

        UsinaSolarFlutuante flutuante = new UsinaSolarFlutuante("UF-01", "Tres Marias", 100.0, 200, 5.0, "Represa TM", 0.10);
        assertEquals(13200.0, flutuante.calcularGeracaoEstimadaMensal(), 0.01);
        assertEquals("Represa TM", flutuante.getReservatorioHidrico());
        assertEquals(0.10, flutuante.getBonusResfriamentoNatural(), 0.01);
    }

    @Test
    @DisplayName("UsinaEolica - Deve testar limites de velocidade segura e geracao")
    void deveTestarUsinaEolicaEValoresLimiteVento() {
        UsinaEolica ventoFraco = new UsinaEolica("UE-01", "Caetite", 100.0, 4, 2.9);
        UsinaEolica ventoMinimo = new UsinaEolica("UE-02", "Caetite", 100.0, 4, 3.0);
        UsinaEolica ventoMaximo = new UsinaEolica("UE-03", "Caetite", 100.0, 4, 25.0);
        UsinaEolica ventoForte = new UsinaEolica("UE-04", "Caetite", 100.0, 4, 25.1);

        assertFalse(ventoFraco.verificarVelocidadeSegura());
        assertTrue(ventoMinimo.verificarVelocidadeSegura());
        assertTrue(ventoMaximo.verificarVelocidadeSegura());
        assertFalse(ventoForte.verificarVelocidadeSegura());

        assertEquals(5400.0, ventoMinimo.calcularGeracaoEstimadaMensal(), 0.01);
        assertEquals(4, ventoMinimo.getNumeroAerogeradores());
        assertEquals(3.0, ventoMinimo.getVelocidadeMediaVentoMs(), 0.01);
    }

    @Test
    @DisplayName("ClienteConsumidor - Deve retornar custo de disponibilidade para fases 1, 2 e 3")
    void deveTestarClienteConsumidorPorFase() {
        ClienteConsumidor mono = new ClienteConsumidor("11111111111", "Ana", "RESIDENCIAL", 1);
        ClienteConsumidor bi = new ClienteConsumidor("22222222222", "Bruno", "COMERCIAL", 2);
        ClienteConsumidor tri = new ClienteConsumidor("33333333333", "Industria X", "INDUSTRIAL", 3);

        assertEquals(30.0, mono.obterCustoDisponibilidadeMinimoKwh());
        assertEquals(50.0, bi.obterCustoDisponibilidadeMinimoKwh());
        assertEquals(100.0, tri.obterCustoDisponibilidadeMinimoKwh());

        assertEquals("11111111111", mono.getDocumentoCpfCnpj());
        assertEquals("Ana", mono.getNomeRazaoSocial());
        assertEquals("RESIDENCIAL", mono.getClasseConsumo());
        assertEquals(1, mono.getFaseLigacao());
    }

    @Test
    @DisplayName("LeituraMedidor - Deve calcular consumo bruto valido e tratar leitura invertida")
    void deveTestarLeituraMedidor() {
        LeituraMedidor valida = new LeituraMedidor("MED-01", "11/2026", 100.0, 350.0, 80.0);
        LeituraMedidor invertida = new LeituraMedidor("MED-02", "11/2026", 400.0, 350.0, 0.0);

        assertEquals(250.0, valida.calcularConsumoBrutoNoMes(), 0.01);
        assertEquals(0.0, invertida.calcularConsumoBrutoNoMes(), 0.01);

        assertEquals("MED-01", valida.getCodigoMedidor());
        assertEquals("11/2026", valida.getMesReferencia());
        assertEquals(100.0, valida.getLeituraAnteriorKwh(), 0.01);
        assertEquals(350.0, valida.getLeituraAtualKwh(), 0.01);
        assertEquals(80.0, valida.getEnergiaInjetadaKwh(), 0.01);
    }

    @Test
    @DisplayName("CreditoEnergia - Deve testar saldo valido, expirado, zerado e abatimentos total/parcial")
    void deveTestarCreditoEnergia() {
        ClienteConsumidor titular = new ClienteConsumidor("12345678901", "Carlos", "RESIDENCIAL", 1);

        CreditoEnergia zerado = new CreditoEnergia(titular, 0.0, 12);
        assertFalse(zerado.possuiSaldoValido());
        assertEquals(100.0, zerado.abaterConsumo(100.0), 0.01);

        CreditoEnergia expirado = new CreditoEnergia(titular, 50.0, 0);
        assertFalse(expirado.possuiSaldoValido());
        assertEquals(100.0, expirado.abaterConsumo(100.0), 0.01);

        CreditoEnergia suficiente = new CreditoEnergia(titular, 200.0, 6);
        assertTrue(suficiente.possuiSaldoValido());
        assertEquals(0.0, suficiente.abaterConsumo(120.0), 0.01);
        assertEquals(80.0, suficiente.getSaldoAcumuladoKwh(), 0.01);

        // Agora abate parcialmente os 80 kWh restantes contra 150 kWh de consumo
        assertEquals(70.0, suficiente.abaterConsumo(150.0), 0.01);
        assertEquals(0.0, suficiente.getSaldoAcumuladoKwh(), 0.01);
        assertEquals(titular, suficiente.getTitular());
        assertEquals(6, suficiente.getMesesParaExpirar());
    }

    @Test
    @DisplayName("OrdemManutencao - Deve classificar prioridades PREVENTIVA, ALTA e EMERGENCIAL nas fronteiras")
    void deveTestarOrdemManutencao() {
        Inversor inv = new Inversor("INV-1", "WEG", 50.0, null);

        OrdemManutencao preventiva = new OrdemManutencao("OS-1", inv, 39);
        assertEquals("PREVENTIVA", preventiva.getPrioridade());
        assertEquals(250.0, preventiva.getCustoEstimado(), 0.01);

        OrdemManutencao alta = new OrdemManutencao("OS-2", inv, 40);
        assertEquals("ALTA", alta.getPrioridade());
        assertEquals(650.0, alta.getCustoEstimado(), 0.01);

        OrdemManutencao emergencial = new OrdemManutencao("OS-3", inv, 75);
        assertEquals("EMERGENCIAL", emergencial.getPrioridade());
        assertEquals(1500.0, emergencial.getCustoEstimado(), 0.01);
        assertEquals("OS-3", emergencial.getProtocolo());
        assertEquals(inv, emergencial.getInversorAlvo());
    }
}
