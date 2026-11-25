package br.edu.solargrid.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class WebControllersMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / - Deve renderizar Dashboard com indicadores carregados")
    void deveExibirDashboard() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("totalUsinas", "potenciaTotal", "totalFaturas", "totalOrdens"));
    }

    @Test
    @DisplayName("GET e POST /usinas - Deve listar e cadastrar os 3 tipos de usina (SOLAR, FLUTUANTE, EOLICA)")
    void deveTestarFluxoUsinas() throws Exception {
        mockMvc.perform(get("/usinas"))
                .andExpect(status().isOk())
                .andExpect(view().name("usinas"))
                .andExpect(model().attributeExists("usinas"));

        mockMvc.perform(post("/usinas/cadastrar")
                        .param("tipo", "SOLAR")
                        .param("codigo", "US-T1")
                        .param("municipio", "Juiz de Fora")
                        .param("potenciaKw", "120"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemSucesso"));

        mockMvc.perform(post("/usinas/cadastrar")
                        .param("tipo", "FLUTUANTE")
                        .param("codigo", "UF-T2")
                        .param("municipio", "Furnas")
                        .param("potenciaKw", "300"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemSucesso"));

        mockMvc.perform(post("/usinas/cadastrar")
                        .param("tipo", "EOLICA")
                        .param("codigo", "UE-T3")
                        .param("municipio", "Bahia")
                        .param("potenciaKw", "800"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemSucesso"));
    }

    @Test
    @DisplayName("GET e POST /inversores - Deve exibir tela e gerar diagnostico com ordem de manutencao")
    void deveTestarFluxoInversores() throws Exception {
        mockMvc.perform(get("/inversores"))
                .andExpect(status().isOk())
                .andExpect(view().name("inversores"));

        mockMvc.perform(post("/inversores/diagnosticar")
                        .param("numeroSerie", "INV-TESTE")
                        .param("fabricante", "Fronius")
                        .param("temperaturaC", "90.0")
                        .param("tensaoV", "220.0")
                        .param("eficienciaPct", "92.0")
                        .param("horasContinuas", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("inversores"))
                .andExpect(model().attribute("ultimoScore", 100))
                .andExpect(model().attributeExists("ultimaOrdem"));
    }

    @Test
    @DisplayName("POST /faturas - Deve cobrir validacoes extras (CPF vazio, credito negativo, bandeira 0) e limpar lista")
    void deveCobrirBranchesExtrasDeFaturasELimpar() throws Exception {
        mockMvc.perform(post("/faturas/emitir")
                        .param("nomeCliente", "Joao")
                        .param("cpfCnpj", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemErro"));

        mockMvc.perform(post("/faturas/emitir")
                        .param("nomeCliente", "Joao")
                        .param("cpfCnpj", "12345678901")
                        .param("consumoKwh", "100")
                        .param("creditoKwh", "-10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemErro"));

        mockMvc.perform(post("/faturas/emitir")
                        .param("nomeCliente", "Joao")
                        .param("cpfCnpj", "12345678901")
                        .param("consumoKwh", "100")
                        .param("creditoKwh", "0")
                        .param("bandeira", "0"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("mensagemErro"));

        mockMvc.perform(post("/faturas/limpar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/faturas"));
    }
}
