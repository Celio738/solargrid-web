package br.edu.solargrid.controller;

import br.edu.solargrid.service.SolarGridDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SolarGridDataService dataService;

    public DashboardController(SolarGridDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalUsinas", dataService.getUsinas().size());
        model.addAttribute("potenciaTotal", dataService.calcularPotenciaTotalInstalada());
        model.addAttribute("totalFaturas", dataService.getFaturasEmitidas().size());
        model.addAttribute("totalFaturado", dataService.calcularTotalFaturado());
        model.addAttribute("totalOrdens", dataService.getOrdensManutencao().size());
        model.addAttribute("ordensEmergenciais", dataService.contarOrdensEmergenciais());
        return "index";
    }
}
