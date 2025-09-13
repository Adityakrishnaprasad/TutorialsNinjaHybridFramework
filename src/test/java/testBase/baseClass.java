package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import utilities.AllureEnvironmentWriter;
import utilities.configurationReader;
import utilities.LoggerLoad;

public class baseClass {

    // ThreadLocal WebDriver
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    // ✅ still expose driver, so no need to change test/page code
    public static WebDriver driver;

    private static void setDriver(WebDriver driverInstance) {
        tlDriver.set(driverInstance);
        driver = tlDriver.get();   // driver always points to current thread’s WebDriver
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    @Parameters({"browser"})
    @BeforeTest(alwaysRun = true)
    public void setUp(String browser) throws MalformedURLException {

        LoggerLoad.info("===== Test Execution Started =====");
        LoggerLoad.info("Selected Browser: " + browser);

        WebDriver driverInstance = null;

        // Grid environment
        if (configurationReader.get("execution_env").equalsIgnoreCase("remote")) {
            LoggerLoad.info("Running scripts in grid environment");
            DesiredCapabilities dcp = new DesiredCapabilities();

            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions co = new ChromeOptions();
                    co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    co.addArguments("--start-maximized", "--remote-allow-origins=*");
                    dcp.setCapability(ChromeOptions.CAPABILITY, co);
                    dcp.setBrowserName("chrome");
                    driverInstance = new RemoteWebDriver(new URL(configurationReader.get("gridURL")), dcp);
                    break;

                case "edge":
                    EdgeOptions eo = new EdgeOptions();
                    eo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    dcp.setCapability(EdgeOptions.CAPABILITY, eo);
                    dcp.setBrowserName("MicrosoftEdge");
                    driverInstance = new RemoteWebDriver(new URL(configurationReader.get("gridURL")), dcp);
                    break;

                case "firefox":
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    dcp.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fo);
                    dcp.setBrowserName("firefox");
                    driverInstance = new RemoteWebDriver(new URL(configurationReader.get("gridURL")), dcp);
                    break;

                default:
                    throw new IllegalArgumentException("Choose a valid browser");
            }

            driverInstance.manage().window().maximize();
        }

        // Local environment
        else if (configurationReader.get("execution_env").equalsIgnoreCase("local")) {
            LoggerLoad.info("Running scripts locally");

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions co = new ChromeOptions();
                    co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    co.addArguments("--start-maximized", "--remote-allow-origins=*");
                    driverInstance = new ChromeDriver(co);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    driverInstance = new FirefoxDriver(fo);
                    driverInstance.manage().window().maximize();
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions eo = new EdgeOptions();
                    eo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    driverInstance = new EdgeDriver(eo);
                    driverInstance.manage().window().maximize();
                    break;

                default:
                    LoggerLoad.error("Unsupported browser provided: " + browser);
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        }

        //  store driver in ThreadLocal
        setDriver(driverInstance);

        // Common setup
        driver.manage().deleteAllCookies();
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
        WebDriver driverInstance = getDriver();
        if (driverInstance != null) {
            LoggerLoad.info("Closing the browser and quitting WebDriver...");
            driverInstance.quit();
            tlDriver.remove();
            LoggerLoad.info("===== Test Execution Finished =====");
        }
    }
}
