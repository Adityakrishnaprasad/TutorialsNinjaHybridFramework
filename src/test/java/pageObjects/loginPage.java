package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class loginPage extends basePage{

	public loginPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(id="input-email") WebElement emailField;
	@FindBy(id="input-password") WebElement passwordField;
	@FindBy(xpath="//input[@value='Login']") WebElement loginButton;
	@FindBy(xpath="//h2[text()='My Account']") WebElement myAccountHeader;
	
	public void enterEmail(String email) {
		emailField.clear();
		emailField.sendKeys(email);
	}
	
	public void enterPassword(String password) {
		passwordField.clear();
		passwordField.sendKeys(password);
	}
	
	public void clickLoginButton() {
		loginButton.click();
	}
	
	public void verifyLoginSuccess() {
		Assert.assertTrue(myAccountHeader.isDisplayed(), "Login failed, My Account header not displayed.");
	}

}
