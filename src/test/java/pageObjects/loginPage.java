package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class loginPage extends basePage{

	public loginPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(id="input-email") private WebElement emailField;
    @FindBy(id="input-password") private WebElement passwordField;
    @FindBy(xpath="//input[@value='Login']") private WebElement loginButton;
    @FindBy(xpath="//h2[text()='My Account']") private WebElement myAccountHeader;

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
    	customWait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void verifyLoginSuccess() {
 	   // Wait for the My Account header to be visible after login
    	customWait.until(ExpectedConditions.visibilityOf(myAccountHeader));
        Assert.assertTrue(myAccountHeader.isDisplayed(), "Login failed, My Account header not displayed.");
    }

}
