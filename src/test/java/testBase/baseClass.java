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

	// ThreadLocal WebDriver (ONLY source of driver)
	private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	private static void setDriver(WebDriver driver) {
		tlDriver.set(driver);
	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	@Parameters({ "browser" })
	@BeforeTest(alwaysRun = true)
	public void setUp(String browser) throws MalformedURLException {

		LoggerLoad.info("===== Test Execution Started =====");
		LoggerLoad.info("Selected Browser: " + browser);
		LoggerLoad.info("Thread ID: " + Thread.currentThread().getId());

		String executionEnv = configurationReader.get("execution_env");
		LoggerLoad.info("DEBUG execution_env raw value = [" + executionEnv + "]");

		if (executionEnv == null) {
			executionEnv = "local";
		}
		executionEnv = executionEnv.trim().toLowerCase();

		// ================= GRID ENVIRONMENT =================
		if (executionEnv.equals("remote")) {

			LoggerLoad.info("Running scripts in grid environment");

			String hubURL = System.getenv("SELENIUM_HUB_URL");
			if (hubURL == null || hubURL.isEmpty()) {
				hubURL = configurationReader.get("gridURL");
			}

			LoggerLoad.info("Using Hub URL: " + hubURL);
			DesiredCapabilities dcp = new DesiredCapabilities();

			switch (browser.toLowerCase()) {

			case "chrome": {
				ChromeOptions co = new ChromeOptions();
				co.setPageLoadStrategy(PageLoadStrategy.EAGER);
				co.addArguments("--remote-allow-origins=*");

				dcp.setCapability(ChromeOptions.CAPABILITY, co);
				dcp.setBrowserName("chrome");

				setDriver(new RemoteWebDriver(new URL(hubURL), dcp));
				break;
			}

			case "firefox": {
				FirefoxOptions fo = new FirefoxOptions();
				fo.setPageLoadStrategy(PageLoadStrategy.EAGER);

				dcp.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fo);
				dcp.setBrowserName("firefox");

				setDriver(new RemoteWebDriver(new URL(hubURL), dcp));
				break;
			}

			case "edge": {
				EdgeOptions eo = new EdgeOptions();
				eo.setPageLoadStrategy(PageLoadStrategy.EAGER);

				dcp.setCapability(EdgeOptions.CAPABILITY, eo);
				dcp.setBrowserName("MicrosoftEdge");

				setDriver(new RemoteWebDriver(new URL(hubURL), dcp));
				break;
			}

			default:
				throw new IllegalArgumentException("Choose a valid browser");
			}
		}

		// ================= LOCAL ENVIRONMENT =================
		else {

			LoggerLoad.info("Running scripts locally");

			switch (browser.toLowerCase()) {

			case "chrome":
				WebDriverManager.chromedriver().setup();
				setDriver(new ChromeDriver());
				break;

			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				setDriver(new FirefoxDriver());
				break;

			case "edge":
				WebDriverManager.edgedriver().setup();
				setDriver(new EdgeDriver());
				break;

			default:
				throw new IllegalArgumentException("Browser not supported: " + browser);
			}
		}

		// ================= COMMON SETUP =================
		getDriver().manage().deleteAllCookies();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
		getDriver().manage().window().maximize();

		String url = configurationReader.get("baseURL");
		LoggerLoad.info("Navigating to: " + url);
		getDriver().get(url);

		AllureEnvironmentWriter.writeEnv(getDriver());
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
	public void tearDown() {

		if (getDriver() != null) {
			LoggerLoad.info("Closing browser for Thread ID: " + Thread.currentThread().getId());
			try {
				getDriver().quit();
			} finally {
				tlDriver.remove();
			}
			LoggerLoad.info("===== Test Execution Finished =====");
		}
	}
}
