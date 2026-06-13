package pageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.LoggerLoad; 

public class basePage {

    protected WebDriver driver;
    protected WebDriverWait customWait;

    // default config - tune these values
   

    public basePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        customWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        LoggerLoad.info("Initializing Page Object: " + this.getClass().getSimpleName());

    }
}
