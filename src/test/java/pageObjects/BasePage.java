package pageObjects;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.LoggerLoad; 

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait customWait;

    // Header "My Account" menu - the same on every page, so defined once here
    @FindBy(xpath = "//a[@title='My Account']")
    private WebElement myAccountMenu;

    @FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]//a[text()='My Account']")
    private WebElement myAccountMenuLink;

    // Maximum time to wait for an element (explicit wait)
    private static final int WAIT_SECONDS = 30;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        customWait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        LoggerLoad.info("Initializing Page Object: " + this.getClass().getSimpleName());
    }

    // Opens the header "My Account" menu (shows Register/Login or My Account/Logout)
    public void openMyAccountMenu() {
        LoggerLoad.info("Opening 'My Account' menu");
        click(myAccountMenu);
    }

    // Opens the menu and goes to the My Account page
    public void goToMyAccountPage() {
        openMyAccountMenu();
        LoggerLoad.info("Going to 'My Account' page");
        click(myAccountMenuLink);
    }

    // Waits until the field is visible, then clears it and types the text
    protected void type(WebElement element, String text) {
        customWait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    // Waits until the element is clickable, scrolls it to the centre of the screen
    // (so sticky headers can't cover it), then does a real click like a user.
    // No JavaScript click fallback: if a user couldn't click it, the test should fail.
    protected void click(WebElement element) {
        customWait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        element.click();
    }
}
