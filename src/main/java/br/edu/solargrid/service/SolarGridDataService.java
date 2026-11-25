package br.edu.solargrid.service;

import br.edu.solargrid.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SolarGridDataService {

    private final List<UnidadeGeradora> usinas = new ArrayList<>();
    private final List<OrdemManutencao> ordensManutencao = new ArrayList<>();
    private final List<FaturaEnergia> faturasEmitidas = new ArrayList<>();
    private final AtomicLong geradorIdFatura = new AtomicLong(1);
    private final AtomicLong geradorProtocoloOs = new AtomicLong(1001);

    public SolarGridDataService() {
        // Dados iniciais de exemplo para enriquecer a apresentacao
        usinas.add(new UsinaSolarFotovoltaica("US-JF-01", "Juiz de Fora", 250.0, 500, 5.2));
        usinas.add(new UsinaSolarFlutuante("UF-MG-02", "Tres Marias", 400.0, 800, 5.5, "Represa Tres Marias", 0.12));
        usinas.add(new UsinaEolica("UE-BA-03", "Caetite", 1200.0, 4, 11.5));
    }

    public List<UnidadeGeradora> getUsinas() { return usinas; }
    public List<OrdemManutencao> getOrdensManutencao() { return ordensManutencao; }
    public List<FaturaEnergia> getFaturasEmitidas() { return faturasEmitidas; }

    public long proximoIdFatura() { return geradorIdFatura.getAndIncrement(); }
    public String proximoProtocoloOs() { return "OS-" + geradorProtocoloOs.getAndIncrement(); }

    public double calcularPotenciaTotalInstalada() {
        return usinas.stream().mapToDouble(UnidadeGeradora::getPotenciaInstaladaKw).sum();
    }

    public double calcularTotalFaturado() {
        return Math.round(faturasEmitidas.stream().mapToDouble(FaturaEnergia::getValorTotalCalculado).sum() * 100.0) / 100.0;
    }

    public long contarOrdensEmergenciais() {
        return ordensManutencao.stream().filter(o -> "EMERGENCIAL".equals(o.getPrioridade())).count();
    }
}
