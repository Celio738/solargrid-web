package br.edu.solargrid.controller;

import br.edu.solargrid.domain.*;
import br.edu.solargrid.service.SolarGridDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/faturas")
public class FaturaWebController {

    private final SolarGridDataService dataService;

    public FaturaWebController(SolarGridDataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public String exibirPainel(Model model) {
        model.addAttribute("faturas", dataService.getFaturasEmitidas());
        return "faturas";
    }

    @PostMapping("/emitir")
    public String emitirFatura(
            @RequestParam(name = "cpfCnpj", defaultValue = "") String cpfCnpj,
            @RequestParam(name = "nomeCliente", defaultValue = "") String nomeCliente,
            @RequestParam(name = "consumoKwh", defaultValue = "0") double consumoKwh,
            @RequestParam(name = "creditoKwh", defaultValue = "0") double creditoKwh,
            @RequestParam(name = "bandeira", defaultValue = "1") int bandeira,
            @RequestParam(name = "diasAtraso", defaultValue = "0") int diasAtraso,
            Model model) {

        if (nomeCliente.trim().isEmpty() || cpfCnpj.trim().isEmpty()) {
            model.addAttribute("mensagemErro", "Erro: Nome do cliente e CPF/CNPJ sao obrigatorios.");
            model.addAttribute("faturas", dataService.getFaturasEmitidas());
            return "faturas";
        }

        if (cpfCnpj.trim().length() < 11) {
            model.addAttribute("mensagemErro", "Erro: CPF/CNPJ invalido (minimo 11 digitos).");
            model.addAttribute("faturas", dataService.getFaturasEmitidas());
            return "faturas";
        }

        if (consumoKwh < 0 || creditoKwh < 0) {
            model.addAttribute("mensagemErro", "Erro: Valores de consumo ou credito nao podem ser negativos.");
            model.addAttribute("faturas", dataService.getFaturasEmitidas());
            return "faturas";
        }

        if (bandeira < 1 || bandeira > 4) {
            model.addAttribute("mensagemErro", "Erro: Bandeira tarifaria deve estar entre 1 e 4.");
            model.addAttribute("faturas", dataService.getFaturasEmitidas());
            return "faturas";
        }

        ClienteConsumidor cliente = new ClienteConsumidor(cpfCnpj, nomeCliente, "RESIDENCIAL", 1);
        LeituraMedidor leitura = new LeituraMedidor("MED-01", "11/2026", 0, consumoKwh, creditoKwh);
        CreditoEnergia credito = new CreditoEnergia(cliente, creditoKwh, 12);

        FaturaEnergia novaFatura = new FaturaEnergia(
                dataService.proximoIdFatura(), cliente, leitura, credito, bandeira, diasAtraso
        );
        double valorFinal = novaFatura.calcularValorFinalFatura(consumoKwh, creditoKwh, bandeira, diasAtraso);
        dataService.getFaturasEmitidas().add(novaFatura);

        model.addAttribute("mensagemSucesso", "Fatura emitida com sucesso! Valor: R$ " + valorFinal);
        model.addAttribute("faturas", dataService.getFaturasEmitidas());
        return "faturas";
    }

    @PostMapping("/limpar")
    public String limparFaturas() {
        dataService.getFaturasEmitidas().clear();
        return "redirect:/faturas";
    }
}
