package testBase;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import io.qameta.allure.Allure;
import utilities.AllureEnvironmentWriter;
import utilities.LoggerLoad;
import utilities.ConfigurationReader;

public class BaseClass {

	// ThreadLocal WebDriver
	private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	// Keys for the account created in this run (stored per browser in the TestNG test context)
	protected static final String USER_EMAIL = "userEmail";
	protected static final String USER_PASSWORD = "userPassword";

	/**
	 * @param driver
	 */
	private static void setDriver(WebDriver driver) {
		tlDriver.set(driver);
	}

	/**
	 * @return WebDriver
	 */
	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * Starts the browser for this <test> block, locally or on the Selenium Grid.
	 * @param browser chrome, firefox or edge (from the TestNG XML)
	 */
	@Parameters({ "browser" })
	@BeforeTest(alwaysRun = true)
	public void setUp(String browser) throws MalformedURLException {

		LoggerLoad.info("===== Test Execution Started =====");
		LoggerLoad.info("Selected Browser: " + browser);

		// Command line (-Dexecution_env=...) wins over .env
		String executionEnv = System.getProperty("execution_env");
		if (executionEnv == null || executionEnv.isEmpty()) {
			executionEnv = ConfigurationReader.get("execution_env");
		}
		if (executionEnv == null) {
			throw new IllegalStateException("execution_env is not set. Use 'local' or 'remote'.");
		}

		boolean headless = System.getenv("JENKINS_HOME") != null; // headless only on Jenkins
		MutableCapabilities options = buildOptions(browser, headless);

		if (executionEnv.equalsIgnoreCase("remote")) {
			// SELENIUM_HUB_URL (Docker) wins over gridURL from .env
			String hubURL = System.getenv("SELENIUM_HUB_URL");
			if (hubURL == null || hubURL.isEmpty()) {
				hubURL = ConfigurationReader.getRequired("gridURL");
			}
			LoggerLoad.info("Running scripts in grid environment");
			LoggerLoad.info("Using Hub URL: " + hubURL);
			setDriver(new RemoteWebDriver(new URL(hubURL), options));
		} else if (executionEnv.equalsIgnoreCase("local")) {
			LoggerLoad.info("Running scripts locally");
			setDriver(createLocalDriver(browser, options));
		} else {
			throw new IllegalArgumentException("Unknown execution_env: " + executionEnv + ". Use 'local' or 'remote'.");
		}

		if (!headless) {
			getDriver().manage().window().maximize();
		}

		// Common setup
		getDriver().manage().deleteAllCookies();
		// No implicit wait: pages use explicit waits only (see BasePage), so timing stays predictable
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));

		String url = ConfigurationReader.getRequired("baseURL");
		LoggerLoad.info("Navigating to: " + url);
		getDriver().get(url);

		LoggerLoad.info("Writing Allure environment details...");
		AllureEnvironmentWriter.writeEnv(getDriver());

		LoggerLoad.info("Browser setup completed successfully.");
	}

	// Browser options, built once per browser and used for both local and grid runs
	private MutableCapabilities buildOptions(String browser, boolean headless) {
		switch (browser.toLowerCase()) {
		case "chrome": {
			ChromeOptions options = new ChromeOptions();
			applyChromiumSettings(options, headless);
			options.addArguments("--remote-allow-origins=*");
			return options;
		}
		case "edge": {
			EdgeOptions options = new EdgeOptions();
			applyChromiumSettings(options, headless);
			return options;
		}
		case "firefox": {
			FirefoxOptions options = new FirefoxOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			if (headless) {
				options.addArguments("-headless");
			}
			return options;
		}
		default:
			LoggerLoad.error("Unsupported browser provided: " + browser);
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}
	}

	// Settings shared by Chrome and Edge (both are Chromium browsers)
	private void applyChromiumSettings(ChromiumOptions<?> options, boolean headless) {
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		if (headless) {
			options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox",
					"--window-size=1920,1080", "--remote-debugging-port=0");
		}
	}

	private WebDriver createLocalDriver(String browser, MutableCapabilities options) {
		switch (browser.toLowerCase()) {
		case "chrome":
			return new ChromeDriver((ChromeOptions) options);
		case "edge":
			return new EdgeDriver((EdgeOptions) options);
		default:
			return new FirefoxDriver((FirefoxOptions) options);
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void startTestLog() {
		LoggerLoad.startTestLog();
	}

	// Attaches this test's own log lines to Allure, plus a screenshot when the test failed
	@AfterMethod(alwaysRun = true)
	public void attachToAllure(ITestResult result) {
		String name = result.getMethod().getMethodName();
		try {
			if (result.getStatus() == ITestResult.FAILURE && getDriver() != null) {
				byte[] png = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
				Allure.addAttachment(name + " - screenshot", "image/png", new ByteArrayInputStream(png), "png");
			}
		} catch (Exception e) {
			LoggerLoad.warn("Could not take screenshot: " + e.getMessage());
		}
		Allure.addAttachment(name + " - log", "text/plain", LoggerLoad.getTestLog(), "txt");
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
