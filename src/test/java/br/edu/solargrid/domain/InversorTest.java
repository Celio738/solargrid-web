package br.edu.solargrid.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InversorTest {

    private Inversor inversor;

    @BeforeEach
    void setUp() {
        UsinaSolarFotovoltaica usina = new UsinaSolarFotovoltaica("US-01", "Juiz de Fora", 100.0, 200, 5.2);
        inversor = new Inversor("INV-900", "WEG", 75.0, usina);
    }

    @Test
    @DisplayName("CT01 - Limite inferior congelamento (temp = -10.1)")
    void deveRetornarRiscoMaximoQuandoTemperaturaAbaixoDeMenosDez() {
        assertEquals(100, inversor.diagnosticarIndiceRiscoOperacional(-10.1, 220.0, 95.0, 100));
    }

    @Test
    @DisplayName("CT02 - Fronteira exata -10.0C, tensao normal, eficiencia 90% e 720h")
    void deveCalcularRiscoMinimoNasFronteirasNormais() {
        assertEquals(5, inversor.diagnosticarIndiceRiscoOperacional(-10.0, 220.0, 90.0, 720));
    }

    @Test
    @DisplayName("CT03 - Limite superior superaquecimento (temp = 85.1)")
    void deveRetornarRiscoMaximoQuandoTemperaturaAcimaDeOitentaECinco() {
        assertEquals(100, inversor.diagnosticarIndiceRiscoOperacional(85.1, 220.0, 95.0, 100));
    }

    @Test
    @DisplayName("CT04 - Fronteira 45.0C, subtensao severa (179.9V), efic baixa (74.9%) e > 720h (721h)")
    void deveAvaliarLimitesDeSubtensaoSeveraEEficienciaCritica() {
        assertEquals(75, inversor.diagnosticarIndiceRiscoOperacional(45.0, 179.9, 74.9, 721));
    }

    @Test
    @DisplayName("CT05 - Fronteira 45.1C, subtensao moderada (180.0V) e eficiencia limite 75.0%")
    void deveAvaliarTransicaoTemperaturaModeradaESubtensaoLeve() {
        assertEquals(45, inversor.diagnosticarIndiceRiscoOperacional(45.1, 180.0, 75.0, 720));
    }

    @Test
    @DisplayName("CT06 - Fronteira 65.0C, sobretensao severa (260.1V) e eficiencia 89.9%")
    void deveAvaliarLimiteSessentaECincoGrausESobretensaoSevera() {
        assertEquals(65, inversor.diagnosticarIndiceRiscoOperacional(65.0, 260.1, 89.9, 500));
    }

    @Test
    @DisplayName("CT07 - Fronteira 65.1C, sobretensao moderada (240.1V) e eficiencia 90.0%")
    void deveAvaliarTemperaturaAltaESobretensaoModerada() {
        assertEquals(55, inversor.diagnosticarIndiceRiscoOperacional(65.1, 240.1, 90.0, 500));
    }

    @Test
    @DisplayName("CT08 - Fronteira exata 85.0C e tensao 200.0V")
    void deveAvaliarFronteiraSuperiorTemperaturaOperavel() {
        assertEquals(40, inversor.diagnosticarIndiceRiscoOperacional(85.0, 200.0, 95.0, 500));
        assertEquals("INV-900", inversor.getNumeroSerie());
        assertEquals("WEG", inversor.getFabricante());
        assertEquals(75.0, inversor.getPotenciaNominalKw());
        assertEquals("US-01", inversor.getUsinaAlocada().getCodigoIdentificacao());
    }
}
