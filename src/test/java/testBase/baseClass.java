package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.qameta.allure.Allure;
import utilities.AllureEnvironmentWriter;
import utilities.configurationReader;
import utilities.LoggerLoad; // <-- import logger

public class baseClass {

    public static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    @Parameters({"browser"})
    @BeforeTest(alwaysRun = true)
    public void setUp(String browser) {

        LoggerLoad.info("===== Test Execution Started =====");
        LoggerLoad.info("Selected Browser: " + browser);

         System.out.println();

        switch (browser.toLowerCase()) {
            case "chrome":
                LoggerLoad.info("Launching Chrome browser...");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                LoggerLoad.info("Launching Firefox browser...");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                driver.manage().window().maximize();
                break;

            case "edge":
                LoggerLoad.info("Launching Edge browser...");
                System.setProperty("webdriver.edge.driver", "D:\\edgedriver_win64\\msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                driver.manage().window().maximize();
                break;

            default:
                LoggerLoad.error("Unsupported browser provided: " + browser);
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));

        String url = configurationReader.get("baseURL");
        LoggerLoad.info("Navigating to: " + url);
        driver.get(url);

        LoggerLoad.info("Writing Allure environment details...");
        AllureEnvironmentWriter.writeEnv(driver);

        LoggerLoad.info("Browser setup completed successfully.");
    }
    
    
    @AfterMethod(alwaysRun = true)
    public void attachLogsToAllure(ITestResult result) {
        try {
            File logFile = new File("logs/framework.log");
            if (logFile.exists()) {
                try (FileInputStream is = new FileInputStream(logFile)) {
                    Allure.addAttachment(result.getMethod().getMethodName() + "-logs", is);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to attach logs: " + e.getMessage());
        }
    }
    

    @AfterTest(alwaysRun = true)
    
    public void TearDown() {
        if (driver != null) {
            LoggerLoad.info("Closing the browser and quitting WebDriver...");
            driver.quit();
            LoggerLoad.info("===== Test Execution Finished =====");
        }
    }
}
