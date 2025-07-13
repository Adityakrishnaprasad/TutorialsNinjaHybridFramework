package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class myAccountPage extends basePage{

	public myAccountPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath="//span[text()='My Account']") WebElement myAccountDropdown;
	@FindBy(xpath="//a[text()='Logout']") WebElement logoutLink;
	@FindBy(xpath="//a[text()='Continue']") WebElement continueButton;
	
	public void clickOnMyAccountDropdown() {
		myAccountDropdown.click();
	}
	
	public void clickOnLogout() {
		logoutLink.click();
	}
	
	public void clickOnContinue() {
		continueButton.click();
	}
	

}
