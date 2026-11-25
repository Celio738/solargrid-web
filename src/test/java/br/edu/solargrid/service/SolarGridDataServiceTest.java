package br.edu.solargrid.service;

import br.edu.solargrid.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolarGridDataServiceTest {

    @Test
    @DisplayName("Deve inicializar usinas de exemplo e calcular agregacoes do dashboard corretamente")
    void deveValidarAgregacoesEGeradoresDoServico() {
        SolarGridDataService service = new SolarGridDataService();

        assertEquals(3, service.getUsinas().size());
        assertEquals(1850.0, service.calcularPotenciaTotalInstalada(), 0.01);

        assertEquals(1L, service.proximoIdFatura());
        assertEquals(2L, service.proximoIdFatura());
        assertEquals("OS-1001", service.proximoProtocoloOs());

        // Adiciona ordens (1 emergencial e 1 preventiva)
        Inversor inv = new Inversor("INV-01", "WEG", 50.0, null);
        service.getOrdensManutencao().add(new OrdemManutencao("OS-1002", inv, 80));
        service.getOrdensManutencao().add(new OrdemManutencao("OS-1003", inv, 20));
        assertEquals(1L, service.contarOrdensEmergenciais());

        // Adiciona faturas e valida soma faturada
        FaturaEnergia f1 = new FaturaEnergia(1L, null, null, null, 1, 0);
        f1.calcularValorFinalFatura(100.0, 0.0, 1, 0); // 65.00
        FaturaEnergia f2 = new FaturaEnergia(2L, null, null, null, 1, 0);
        f2.calcularValorFinalFatura(200.0, 0.0, 1, 0); // 170.00
        service.getFaturasEmitidas().add(f1);
        service.getFaturasEmitidas().add(f2);

        assertEquals(235.0, service.calcularTotalFaturado(), 0.01);
    }
}
