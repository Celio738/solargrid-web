package br.edu.solargrid.controller;

import br.edu.solargrid.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/faturas")
public class FaturaWebController {

    private final List<FaturaEnergia> faturasEmitidas = new ArrayList<>();
    private final AtomicLong geradorId = new AtomicLong(1);

    @GetMapping
    public String exibirPainel(Model model) {
        model.addAttribute("faturas", faturasEmitidas);
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
            model.addAttribute("faturas", faturasEmitidas);
            return "faturas";
        }

        if (cpfCnpj.trim().length() < 11) {
            model.addAttribute("mensagemErro", "Erro: CPF/CNPJ invalido (minimo 11 digitos).");
            model.addAttribute("faturas", faturasEmitidas);
            return "faturas";
        }

        if (consumoKwh < 0 || creditoKwh < 0) {
            model.addAttribute("mensagemErro", "Erro: Valores de consumo ou credito nao podem ser negativos.");
            model.addAttribute("faturas", faturasEmitidas);
            return "faturas";
        }

        if (bandeira < 1 || bandeira > 4) {
            model.addAttribute("mensagemErro", "Erro: Bandeira tarifaria deve estar entre 1 e 4.");
            model.addAttribute("faturas", faturasEmitidas);
            return "faturas";
        }

        ClienteConsumidor cliente = new ClienteConsumidor(cpfCnpj, nomeCliente, "RESIDENCIAL", 1);
        LeituraMedidor leitura = new LeituraMedidor("MED-01", "11/2026", 0, consumoKwh, creditoKwh);
        CreditoEnergia credito = new CreditoEnergia(cliente, creditoKwh, 12);

        FaturaEnergia novaFatura = new FaturaEnergia(
                geradorId.getAndIncrement(), cliente, leitura, credito, bandeira, diasAtraso
        );
        double valorFinal = novaFatura.calcularValorFinalFatura(consumoKwh, creditoKwh, bandeira, diasAtraso);
        faturasEmitidas.add(novaFatura);

        model.addAttribute("mensagemSucesso", "Fatura emitida com sucesso! Valor: R$ " + valorFinal);
        model.addAttribute("faturas", faturasEmitidas);
        return "faturas";
    }

    @PostMapping("/limpar")
    public String limparFaturas() {
        faturasEmitidas.clear();
        return "redirect:/faturas";
    }
}
