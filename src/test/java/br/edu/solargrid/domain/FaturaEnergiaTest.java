package br.edu.solargrid.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaturaEnergiaTest {

    private FaturaEnergia fatura;

    @BeforeEach
    void setUp() {
        ClienteConsumidor cliente = new ClienteConsumidor("12345678901", "Empresa Solar", "COMERCIAL", 3);
        LeituraMedidor leitura = new LeituraMedidor("MED-10", "11/2026", 100.0, 300.0, 50.0);
        CreditoEnergia credito = new CreditoEnergia(cliente, 50.0, 12);
        fatura = new FaturaEnergia(1L, cliente, leitura, credito, 1, 0);
    }

    @Test
    @DisplayName("CT01 - Valor limite invalido para consumo negativo (-0.1)")
    void deveLancarExcecaoQuandoConsumoNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                fatura.calcularValorFinalFatura(-0.1, 0.0, 1, 0)
        );
    }

    @Test
    @DisplayName("CT02 - Valor limite invalido para credito negativo (-0.1)")
    void deveLancarExcecaoQuandoCreditoNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                fatura.calcularValorFinalFatura(100.0, -0.1, 1, 0)
        );
    }

    @Test
    @DisplayName("CT03 - Consumo liquido abaixo de 30 kWh (29.9) aplica taxa minima sem atraso")
    void deveAplicarCustoMinimoDisponibilidade() {
        double valor = fatura.calcularValorFinalFatura(29.9, 0.0, 1, 0);
        assertEquals(19.50, valor);
    }

    @Test
    @DisplayName("CT04 - Fronteira 100.0 kWh, Bandeira 2 e 1 dia de atraso (multa 2%)")
    void deveCalcularFaixaCemKwhBandeiraAmarelaComMultaSimples() {
        double valor = fatura.calcularValorFinalFatura(100.0, 0.0, 2, 1);
        assertEquals(68.34, valor);
    }

    @Test
    @DisplayName("CT05 - Fronteira 100.1 kWh, Bandeira 3 e limite 30 dias de atraso (multa 2%)")
    void deveCalcularTransicaoAcimaDeCemKwhBandeiraVermelhaP1() {
        double valor = fatura.calcularValorFinalFatura(100.1, 0.0, 3, 30);
        assertEquals(91.89, valor);
    }

    @Test
    @DisplayName("CT06 - Fronteira 300.0 kWh, Bandeira 4 e 31 dias de atraso (multa 10%)")
    void deveCalcularLimiteTrezentosKwhBandeiraVermelhaP2ComMultaPesada() {
        double valor = fatura.calcularValorFinalFatura(300.0, 0.0, 4, 31);
        assertEquals(306.90, valor);
    }

    @Test
    @DisplayName("CT07 e CT08 - Fronteiras 300.1 kWh e 500.0 kWh")
    void deveCalcularFaixaAteQuinhentosKwh() {
        assertEquals(285.10, fatura.calcularValorFinalFatura(300.1, 0.0, 1, 0));
        assertEquals(475.00, fatura.calcularValorFinalFatura(500.0, 0.0, 1, 0));
    }

    @Test
    @DisplayName("CT09 - Fronteira 500.1 kWh (Acima de 500 kWh)")
    void deveCalcularFaixaAcimaDeQuinhentosKwh() {
        assertEquals(575.12, fatura.calcularValorFinalFatura(500.1, 0.0, 1, 0));
        assertEquals(1L, fatura.getId());
        assertNotNull(fatura.getCliente());
        assertNotNull(fatura.getLeitura());
        assertNotNull(fatura.getCredito());
        assertEquals(1, fatura.getBandeiraTarifaria());
        assertEquals(0, fatura.getDiasAtraso());
        assertEquals(575.12, fatura.getValorTotalCalculado());
    }
}
