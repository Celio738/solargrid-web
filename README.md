# SolarGrid Web

Sistema web de gestão de usinas de energia renovável, diagnóstico de risco operacional de inversores, controle de créditos de geração distribuída e faturamento mensal de energia elétrica.

Projeto desenvolvido como trabalho final da disciplina de **Linguagem de Programação IV** (IF Sudeste MG — Campus Juiz de Fora), com foco em **Métricas Orientadas a Objetos**, **Complexidade Ciclomática** e **Engenharia de Testes**.

## Tecnologias Utilizadas

* **Java 17+** (compatível com Java 21)
* **Spring Boot 3.2.5** (Spring Web & Thymeleaf)
* **Maven**
* **JUnit 5** (Testes Unitários)
* **JaCoCo** (Cobertura de Código)
* **Selenium WebDriver 4** (Testes Funcionais End-to-End)
* **Mockito** (Testes com Objetos Mock)

## Estrutura de Domínio (10 Classes)

O pacote `br.edu.solargrid.domain` contém as 10 classes principais de negócio, estruturadas para avaliação das métricas de **Chidamber & Kemerer (CK)** e **Lorenz & Kidd (LK)**:

* **Hierarquia de Geração:** `UnidadeGeradora` (abstrata) -> `UsinaSolarFotovoltaica` -> `UsinaSolarFlutuante` e `UsinaEolica`.
* **Operação e Manutenção:** `Inversor` e `OrdemManutencao`.
* **Faturamento e Créditos:** `ClienteConsumidor`, `LeituraMedidor`, `CreditoEnergia` e `FaturaEnergia`.
* **Homologação (Mock):** `HomologacaoService` e interface `ConcessionariaTarifaGateway`.

## Destaques de Qualidade e Testes

| Técnica / Ferramenta | Alvo no Sistema | Resultado |
| :--- | :--- | :--- |
| **McCabe (V(G) > 10)** | `Inversor.diagnosticarIndiceRiscoOperacional` e `FaturaEnergia.calcularValorFinalFatura` | V(G) = 12 em ambos |
| **Análise do Valor Limite + JUnit 5** | `InversorTest` (8 testes) e `FaturaEnergiaTest` (8 testes) | 16 casos de teste passando |
| **Cobertura de Código (JaCoCo)** | `Inversor` e `FaturaEnergia` | **100%** (classes, métodos, linhas e branches) |
| **Testes Funcionais (Selenium)** | Tela `/faturas` (`FaturaWebSeleniumTest`) | 5 casos de teste automatizados |
| **Objetos Mock (Mockito)** | `HomologacaoServiceMockTest` | 2 casos de teste com `@Mock` e `verify` |
| **Teste de Integração** | 10 classes de domínio | Estratégia Incremental Top-Down com Stubs |

## Como Executar

### 1. Subir a aplicação web

    mvn spring-boot:run

Acesse no navegador: `http://localhost:8080/faturas`

### 2. Rodar a suíte de testes e gerar relatório de cobertura (JaCoCo)

    mvn test

Relatório HTML gerado em `target/site/jacoco/index.html`.

## Autor

**Célio de Assis Araújo Filho**
