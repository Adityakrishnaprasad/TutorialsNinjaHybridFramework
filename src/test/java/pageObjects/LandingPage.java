package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import utilities.LoggerLoad;

public class LandingPage extends BasePage {

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[normalize-space()='Register']")
    private WebElement registerLink;

    @FindBy(xpath = "//a[normalize-space()='Login']")
    private WebElement loginLink;

    // Opens the My Account menu and clicks Register
    public void goToRegisterPage() {
        openMyAccountMenu();
        LoggerLoad.info("Clicking on 'Register' link");
        click(registerLink);
    }

    // Opens the My Account menu and clicks Login
    public void goToLoginPage() {
        openMyAccountMenu();
        LoggerLoad.info("Clicking on 'Login' link");
        click(loginLink);
    }
}
