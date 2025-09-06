package pageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;


public class basePage {

    protected WebDriver driver;

    // Optional: keep your existing WebDriverWait reference if you already use 'ww' elsewhere
    protected Wait<WebDriver> customWait;

    // default config - tune these values
    protected long customTimeoutSeconds = 20;
    protected long customPollingMillis = 500;

    public basePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        // Initialize customWait (FluentWait) but typed to Wait<WebDriver> so it accepts ExpectedCondition
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }

    // Optional convenience method to adjust config at runtime
    protected void setCustomWaitTimeout(long seconds) {
        this.customTimeoutSeconds = seconds;
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }

    protected void setCustomWaitPolling(long millis) {
        this.customPollingMillis = millis;
        this.customWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(customTimeoutSeconds))
                .pollingEvery(Duration.ofMillis(customPollingMillis))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }
}
