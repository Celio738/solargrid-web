package br.edu.solargrid.controller;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NavegacaoSistemaSeleniumTest {

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

    @Test
    @DisplayName("Selenium E2E - Deve navegar do Dashboard para Usinas e Inversores e atualizar os indicadores")
    void deveExecutarFluxoCompletoEntreDashboardUsinasEInversores() {
        // 1. Acessa o Dashboard inicial
        driver.get("http://localhost:" + port + "/");
        assertTrue(driver.getTitle().contains("Painel Executivo"));

        // 2. Navega para Usinas e cadastra uma Usina Solar Flutuante
        driver.findElement(By.cssSelector("a[href='/usinas']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("codigo")));

        new Select(driver.findElement(By.name("tipo"))).selectByValue("FLUTUANTE");
        driver.findElement(By.name("codigo")).sendKeys("UF-E2E-99");
        driver.findElement(By.name("municipio")).sendKeys("Juiz de Fora");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String msgUsina = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success"))).getText();
        assertTrue(msgUsina.contains("UF-E2E-99"));

        // 3. Navega para Inversores e simula superaquecimento (90C -> EMERGENCIAL)
        driver.findElement(By.cssSelector("a[href='/inversores']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("temperaturaC")));

        WebElement temp = driver.findElement(By.name("temperaturaC"));
        temp.clear();
        temp.sendKeys("90.0");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String resultadoDiagnostico = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("result-box"))).getText();
        assertTrue(resultadoDiagnostico.contains("100/100"));
        assertTrue(resultadoDiagnostico.contains("EMERGENCIAL"));

        // 4. Retorna ao Dashboard e valida que os indicadores refletem os novos dados
        driver.findElement(By.cssSelector("a[href='/']")).click();
        String textoDashboard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("grid"))).getText();
        assertTrue(textoDashboard.contains("Emergenciais ativas: 1"));
    }
}
