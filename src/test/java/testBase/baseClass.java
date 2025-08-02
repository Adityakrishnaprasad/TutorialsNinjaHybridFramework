package testBase;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import utilities.configurationReader;

public class baseClass {

    public static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    @Parameters({"browser"})
    @BeforeTest(alwaysRun = true)
    public void setUp(String browser) {

        boolean isJenkins = System.getenv("JENKINS_HOME") != null;

        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isJenkins) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
            	 FirefoxOptions firefoxOptions = new FirefoxOptions();
                 if (isJenkins) {
                     firefoxOptions.addArguments("--headless");
                     firefoxOptions.addArguments("--disable-gpu");
                     firefoxOptions.addArguments("--window-size=1920,1080");
                 }
                 driver = new FirefoxDriver(firefoxOptions);
                 break;

            case "edge":
            	 System.setProperty("webdriver.edge.driver", "D:\\edgedriver_win64\\msedgedriver.exe");
                 EdgeOptions edgeOptions = new EdgeOptions();
                 if (isJenkins) {
                     edgeOptions.addArguments("--headless=new");
                     edgeOptions.addArguments("--disable-gpu");
                     edgeOptions.addArguments("--window-size=1920,1080");
                     edgeOptions.addArguments("--no-sandbox");
                     edgeOptions.addArguments("--disable-dev-shm-usage");
                 }
                 driver = new EdgeDriver(edgeOptions);
                 break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
                
        }

        driver.manage().window().maximize();
        driver.get(configurationReader.get("baseURL"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterTest(alwaysRun = true)
    public void TearDown() {
        driver.quit();
    }
}
