package br.edu.solargrid.controller;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FaturaWebSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String getUrlBase() {
        return "http://localhost:" + port + "/faturas";
    }

    @Test
    @Order(1)
    @DisplayName("Selenium CT01 - Deve emitir fatura valida com sucesso e exibir na tabela")
    void deveEmitirFaturaValidaComSucesso() {
        driver.get(getUrlBase());

        driver.findElement(By.id("nomeCliente")).sendKeys("Padaria Sol Nascente");
        driver.findElement(By.id("cpfCnpj")).sendKeys("12345678901234");

        WebElement campoConsumo = driver.findElement(By.id("consumoKwh"));
        campoConsumo.clear();
        campoConsumo.sendKeys("250");

        WebElement campoCredito = driver.findElement(By.id("creditoKwh"));
        campoCredito.clear();
        campoCredito.sendKeys("50");

        driver.findElement(By.id("btnEmitir")).click();

        String msgSucesso = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msgSucesso"))).getText();
        String textoTabela = driver.findElement(By.id("tabelaFaturas")).getText();

        assertTrue(msgSucesso.contains("Fatura emitida com sucesso! Valor: R$ 170.0"));
        assertTrue(textoTabela.contains("Padaria Sol Nascente"));
    }

    @Test
    @Order(2)
    @DisplayName("Selenium CT02 - Deve impedir emissao quando nome do cliente estiver vazio")
    void deveExibirErroQuandoNomeClienteVazio() {
        driver.get(getUrlBase());

        driver.findElement(By.id("cpfCnpj")).sendKeys("12345678901");
        driver.findElement(By.id("btnEmitir")).click();

        String msgErro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msgErro"))).getText();
        assertTrue(msgErro.contains("Nome do cliente e CPF/CNPJ sao obrigatorios"));
    }

    @Test
    @Order(3)
    @DisplayName("Selenium CT03 - Deve impedir emissao quando CPF/CNPJ tiver menos de 11 digitos")
    void deveExibirErroQuandoCpfInvalido() {
        driver.get(getUrlBase());

        driver.findElement(By.id("nomeCliente")).sendKeys("Maria Oliveira");
        driver.findElement(By.id("cpfCnpj")).sendKeys("12345");
        driver.findElement(By.id("btnEmitir")).click();

        String msgErro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msgErro"))).getText();
        assertTrue(msgErro.contains("CPF/CNPJ invalido (minimo 11 digitos)"));
    }

    @Test
    @Order(4)
    @DisplayName("Selenium CT04 - Deve impedir emissao quando consumo for negativo")
    void deveExibirErroQuandoConsumoNegativo() {
        driver.get(getUrlBase());

        driver.findElement(By.id("nomeCliente")).sendKeys("Carlos Mendes");
        driver.findElement(By.id("cpfCnpj")).sendKeys("98765432100");

        WebElement campoConsumo = driver.findElement(By.id("consumoKwh"));
        campoConsumo.clear();
        campoConsumo.sendKeys("-50");

        driver.findElement(By.id("btnEmitir")).click();

        String msgErro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msgErro"))).getText();
        assertTrue(msgErro.contains("Valores de consumo ou credito nao podem ser negativos"));
    }

    @Test
    @Order(5)
    @DisplayName("Selenium CT05 - Deve impedir emissao quando bandeira tarifaria for invalida")
    void deveExibirErroQuandoBandeiraInvalida() {
        driver.get(getUrlBase());

        driver.findElement(By.id("nomeCliente")).sendKeys("Condominio Solar");
        driver.findElement(By.id("cpfCnpj")).sendKeys("11222333000199");

        WebElement campoBandeira = driver.findElement(By.id("bandeira"));
        campoBandeira.clear();
        campoBandeira.sendKeys("9");

        driver.findElement(By.id("btnEmitir")).click();

        String msgErro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msgErro"))).getText();
        assertTrue(msgErro.contains("Bandeira tarifaria deve estar entre 1 e 4"));
    }
}
