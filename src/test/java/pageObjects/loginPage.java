package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import utilities.LoggerLoad; 

public class loginPage extends basePage {

    public loginPage(WebDriver driver) {
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
    	System.out.println();
        LoggerLoad.info("Entering email: " + email);
        emailField.clear();
        emailField.sendKeys(email);
    }

    /** 
     * @param password
     */
    public void enterPassword(String password) {
        LoggerLoad.info("Entering password: [PROTECTED]");
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        LoggerLoad.info("Clicking on 'Login' button");
        customWait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void verifyLoginSuccess() {
        LoggerLoad.info("Verifying login success by checking 'My Account' header");
        customWait.until(ExpectedConditions.visibilityOf(myAccountHeader));
        Assert.assertTrue(myAccountHeader.isDisplayed(),
                "Login failed, My Account header not displayed.");
        LoggerLoad.info("Login successful - 'My Account' header is visible");
        System.out.println();
    }
}
