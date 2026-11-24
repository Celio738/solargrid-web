package br.edu.solargrid.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomologacaoServiceMockTest {

    @Mock
    private ConcessionariaTarifaGateway gatewayMock;

    @InjectMocks
    private HomologacaoService homologacaoService;

    @Test
    @DisplayName("Mock CT01 - Deve aprovar homologacao e calcular custo com imposto quando subestacao disponivel")
    void deveCalcularCustoHomologacaoQuandoSubestacaoDisponivel() {
        UsinaSolarFotovoltaica usina = new UsinaSolarFotovoltaica("US-10", "Juiz de Fora", 100.0, 200, 5.0);
        // Taxa base = (100 * 5.0) + (200 * 1.50) = 500 + 300 = 800.0

        when(gatewayMock.verificarSubestacaoDisponivel("Juiz de Fora", 100.0)).thenReturn(true);
        when(gatewayMock.consultarAliquotaImpostoEstadual("Juiz de Fora")).thenReturn(0.18);

        double custoTotal = homologacaoService.calcularCustoHomologacaoUsina(usina);

        assertEquals(944.0, custoTotal, 0.01);
        verify(gatewayMock, times(1)).verificarSubestacaoDisponivel("Juiz de Fora", 100.0);
        verify(gatewayMock, times(1)).consultarAliquotaImpostoEstadual("Juiz de Fora");
    }

    @Test
    @DisplayName("Mock CT02 - Deve lancar excecao quando concessionaria negar capacidade na subestacao")
    void deveLancarExcecaoQuandoSubestacaoIndisponivel() {
        UsinaEolica usina = new UsinaEolica("UE-05", "Barbacena", 500.0, 5, 12.0);

        when(gatewayMock.verificarSubestacaoDisponivel("Barbacena", 500.0)).thenReturn(false);

        assertThrows(IllegalStateException.class, () ->
                homologacaoService.calcularCustoHomologacaoUsina(usina)
        );

        verify(gatewayMock, times(1)).verificarSubestacaoDisponivel("Barbacena", 500.0);
        verify(gatewayMock, never()).consultarAliquotaImpostoEstadual(anyString());
    }
}
