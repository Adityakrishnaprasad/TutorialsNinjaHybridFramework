package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utilities.LoggerLoad;

public class editAccountPage extends basePage {

  
    public editAccountPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]//a[text()='My Account']")
	private WebElement myAccountOption;
	
	@FindBy(xpath = "//a[text()='Edit your account information']")
	private WebElement editAccountInfoLink;
    
    @FindBy(xpath = "//div[contains(@class,'alert-success')]")
    private WebElement successAlert;

    
    public void navigateToMyAccount() {
    	LoggerLoad.info("Navigating to 'My Account' page");
    	customWait.until(ExpectedConditions.elementToBeClickable(myAccountOption)).click();
    	LoggerLoad.info("Clicked on 'My Account' option");
    }
    
    public void navigateToEditAccountPage() {
		customWait.until(ExpectedConditions.elementToBeClickable(editAccountInfoLink)).click();
		LoggerLoad.info("Clicked on 'Edit your account information' link");
	}
    
    public String getSuccessMessage() {
        LoggerLoad.info("Fetching account update success message");
        customWait.until(ExpectedConditions.visibilityOf(successAlert));
        String actualMsg = successAlert.getText().trim();
        LoggerLoad.info("Success message displayed: " + actualMsg);
        return actualMsg;
    }
}
