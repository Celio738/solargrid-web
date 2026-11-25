package br.edu.solargrid.controller;

import br.edu.solargrid.domain.*;
import br.edu.solargrid.service.SolarGridDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usinas")
public class UsinaWebController {

    private final SolarGridDataService dataService;

    public UsinaWebController(SolarGridDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public String listarUsinas(Model model) {
        model.addAttribute("usinas", dataService.getUsinas());
        return "usinas";
    }

    @PostMapping("/cadastrar")
    public String cadastrarUsina(
            @RequestParam(name = "tipo") String tipo,
            @RequestParam(name = "codigo") String codigo,
            @RequestParam(name = "municipio") String municipio,
            @RequestParam(name = "potenciaKw") double potenciaKw,
            @RequestParam(name = "modulos", defaultValue = "100") int modulos,
            @RequestParam(name = "irradiacao", defaultValue = "5.0") double irradiacao,
            @RequestParam(name = "reservatorio", defaultValue = "Reservatorio Local") String reservatorio,
            @RequestParam(name = "bonusResfriamento", defaultValue = "0.10") double bonusResfriamento,
            @RequestParam(name = "aerogeradores", defaultValue = "3") int aerogeradores,
            @RequestParam(name = "velocidadeVento", defaultValue = "10.0") double velocidadeVento,
            Model model) {

        UnidadeGeradora nova;
        if ("FLUTUANTE".equals(tipo)) {
            nova = new UsinaSolarFlutuante(codigo, municipio, potenciaKw, modulos, irradiacao, reservatorio, bonusResfriamento);
        } else if ("EOLICA".equals(tipo)) {
            nova = new UsinaEolica(codigo, municipio, potenciaKw, aerogeradores, velocidadeVento);
        } else {
            nova = new UsinaSolarFotovoltaica(codigo, municipio, potenciaKw, modulos, irradiacao);
        }

        dataService.getUsinas().add(nova);
        model.addAttribute("mensagemSucesso", "Unidade geradora " + codigo + " cadastrada! Geração estimada: "
                + String.format("%.1f", nova.calcularGeracaoEstimadaMensal()) + " kWh/mês.");
        model.addAttribute("usinas", dataService.getUsinas());
        return "usinas";
    }
}
