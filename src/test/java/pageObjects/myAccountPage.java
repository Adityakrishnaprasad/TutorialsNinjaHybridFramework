package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class myAccountPage extends basePage{

	public myAccountPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath="//span[text()='My Account']") WebElement myAccountDropdown;
	@FindBy(xpath="//a[text()='Logout']") WebElement logoutLink;
	@FindBy(xpath="//a[text()='Continue']") WebElement continueButton;
	@FindBy(name="search") WebElement searchField;
	@FindBy(xpath="//a[text()='Samsung Galaxy Tab 10.1']") WebElement prod;
	@FindBy(xpath="//i[@class='fa fa-search']") WebElement searchBtn;
	
	public void clickOnMyAccountDropdown() {
		myAccountDropdown.click();
		
	}
	
	public void clickOnLogout() {
		logoutLink.click();
	}
	
	public void clickOnContinue() {
		continueButton.click();
	}
	
	public void SearchForProduct(String pName) {
		searchField.sendKeys(pName);
		searchBtn.click();
	}
	
	public void clickOnProduct() {
	    try {
	        ww.until(ExpectedConditions.elementToBeClickable(prod)).click();
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript(
	            "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", prod
	        );
	    }
	}
	
	public String getNameOfProduct() {
		return prod.getText();
	}

}
