package pageObjects;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import utilities.LoggerLoad; 

public class basePage {

    protected WebDriver driver;
    protected Wait<WebDriver> customWait;

    // default config - tune these values
    protected long customTimeoutSeconds = 20;
    protected long customPollingMillis = 500;

    public basePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        System.out.println();
        LoggerLoad.info("Initializing Page Object: " + this.getClass().getSimpleName());

        // Initialize customWait (FluentWait)
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);

        LoggerLoad.info("Custom wait configured: timeout=" + customTimeoutSeconds + "s, polling=" + customPollingMillis + "ms");
    }

    // Adjust timeout dynamically
    protected void setCustomWaitTimeout(long seconds) {
        this.customTimeoutSeconds = seconds;
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);

        LoggerLoad.info("Custom wait timeout updated: " + seconds + " seconds");
    }

    // Adjust polling interval dynamically
    protected void setCustomWaitPolling(long millis) {
        this.customPollingMillis = millis;
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);

        LoggerLoad.info("Custom wait polling interval updated: " + millis + " ms");
        System.out.println();
    }
}
