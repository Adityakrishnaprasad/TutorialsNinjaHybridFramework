package pageObjects;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad; 

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "input-email")
    private WebElement emailField;

    @FindBy(id = "input-password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//h2[text()='My Account']")
    private WebElement myAccountHeader;

    /** 
     * @param email
     */
    public void enterEmail(String email) {
        LoggerLoad.info("Entering email: " + email);
        type(emailField, email);
    }

    /** 
     * @param password
     */
    public void enterPassword(String password) {
        LoggerLoad.info("Entering password: [PROTECTED]");
        type(passwordField, password);
    }

    public void clickLoginButton() {
        LoggerLoad.info("Clicking on 'Login' button");
        click(loginButton);
    }

    // True if the 'My Account' page appears after login (waits up to the explicit wait time)
    public boolean isMyAccountPageShown() {
        LoggerLoad.info("Checking for 'My Account' header after login");
        try {
            customWait.until(ExpectedConditions.visibilityOf(myAccountHeader));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
