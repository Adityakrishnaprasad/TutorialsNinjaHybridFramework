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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import utilities.AllureEnvironmentWriter;
import utilities.LoggerLoad;
import utilities.configurationReader;

public class baseClass {

    // ThreadLocal WebDriver
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    // still expose driver, so no need to change test/page code
    public static WebDriver driver;

    private static void setDriver(WebDriver driver) {
        tlDriver.set(driver);
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    @Parameters({"browser"})
    @BeforeTest(alwaysRun = true)
    public void setUp(String browser) throws MalformedURLException {

        LoggerLoad.info("===== Test Execution Started =====");
        LoggerLoad.info("Selected Browser: " + browser);

        // allow environment override via System env var if provided
        // (optional) You already compute hubURL below; this is for completeness.
        // Grid environment
        if (configurationReader.get("execution_env").equalsIgnoreCase("remote")) {
            LoggerLoad.info("Running scripts in grid environment");

            // check if SELENIUM_HUB_URL is provided from Docker
            String hubURL = System.getenv("SELENIUM_HUB_URL");
            if (hubURL == null || hubURL.isEmpty()) {
                hubURL = configurationReader.get("gridURL"); // fallback to config file
            }

            LoggerLoad.info("Using Hub URL: " + hubURL);
            DesiredCapabilities dcp = new DesiredCapabilities();

            switch (browser.toLowerCase()) {
                case "chrome": {
                    ChromeOptions co = new ChromeOptions();
                    co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    co.addArguments("--start-maximized", "--remote-allow-origins=*");
                    dcp.setCapability(ChromeOptions.CAPABILITY, co);
                    dcp.setBrowserName("chrome");
                    driver = new RemoteWebDriver(new URL(hubURL), dcp);
                    setDriver(driver);
                    break;
                }

                case "edge": {
                    EdgeOptions eo = new EdgeOptions();
                    eo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    dcp.setCapability(EdgeOptions.CAPABILITY, eo);
                    dcp.setBrowserName("MicrosoftEdge");
                    driver = new RemoteWebDriver(new URL(hubURL), dcp);
                    setDriver(driver);
                    break;
                }

                case "firefox": {
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    dcp.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fo);
                    dcp.setBrowserName("firefox");
                    driver = new RemoteWebDriver(new URL(hubURL), dcp);
                    setDriver(driver);
                    break;
                }

                default:
                    throw new IllegalArgumentException("Choose a valid browser");
            }

            driver.manage().window().maximize();
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
                    driver = new ChromeDriver(co);
                    setDriver(driver);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions fo = new FirefoxOptions();
                    fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    driver = new FirefoxDriver(fo);
                    setDriver(driver);
                    getDriver().manage().window().maximize();
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions eo = new EdgeOptions();
                    eo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    driver = new EdgeDriver(eo);
                    setDriver(driver);
                    getDriver().manage().window().maximize();
                    break;

                default:
                    LoggerLoad.error("Unsupported browser provided: " + browser);
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        }

        // Common setup
        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));

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

        if (getDriver() != null) {
            LoggerLoad.info("Closing the browser and quitting WebDriver...");
            try {
                getDriver().quit();
            } catch (Exception e) {
                LoggerLoad.error("Error while quitting driver: " + e.getMessage());
            } finally {
                tlDriver.remove();
            }
            LoggerLoad.info("===== Test Execution Finished =====");
        }
    }
}
