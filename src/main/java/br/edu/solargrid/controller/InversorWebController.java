package br.edu.solargrid.controller;

import br.edu.solargrid.domain.*;
import br.edu.solargrid.service.SolarGridDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inversores")
public class InversorWebController {

    private final SolarGridDataService dataService;

    public InversorWebController(SolarGridDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public String exibirDiagnostico(Model model) {
        model.addAttribute("ordens", dataService.getOrdensManutencao());
        return "inversores";
    }

    @PostMapping("/diagnosticar")
    public String diagnosticarInversor(
            @RequestParam(name = "numeroSerie") String numeroSerie,
            @RequestParam(name = "fabricante") String fabricante,
            @RequestParam(name = "potenciaKw", defaultValue = "75.0") double potenciaKw,
            @RequestParam(name = "temperaturaC") double temperaturaC,
            @RequestParam(name = "tensaoV") double tensaoV,
            @RequestParam(name = "eficienciaPct") double eficienciaPct,
            @RequestParam(name = "horasContinuas") int horasContinuas,
            Model model) {

        UsinaSolarFotovoltaica usinaRef = new UsinaSolarFotovoltaica("US-JF-01", "Juiz de Fora", 250.0, 500, 5.2);
        Inversor inversor = new Inversor(numeroSerie, fabricante, potenciaKw, usinaRef);

        int scoreRisco = inversor.diagnosticarIndiceRiscoOperacional(temperaturaC, tensaoV, eficienciaPct, horasContinuas);
        OrdemManutencao ordem = new OrdemManutencao(dataService.proximoProtocoloOs(), inversor, scoreRisco);
        dataService.getOrdensManutencao().add(0, ordem);

        model.addAttribute("ultimoScore", scoreRisco);
        model.addAttribute("ultimaOrdem", ordem);
        model.addAttribute("ordens", dataService.getOrdensManutencao());
        return "inversores";
    }
}
