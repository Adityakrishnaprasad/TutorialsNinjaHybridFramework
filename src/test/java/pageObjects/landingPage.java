package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class landingPage extends basePage{

    public landingPage(WebDriver driver) {
        super(driver);
    }

  
    @FindBy(xpath="//a[@title='My Account']") private WebElement userdd;
    @FindBy(xpath="//a[normalize-space()='Register']") private WebElement regLink;
    @FindBy(xpath="//a[normalize-space()='Login']") private WebElement login;
    

    //clicks on user dropdown and clicks on register user link
    public void clickOnuserReg() {
    	customWait.until(ExpectedConditions.elementToBeClickable(userdd)).click();
    	customWait.until(ExpectedConditions.elementToBeClickable(regLink)).click();
    }
    
    
    //clicks on user dropdown and clicks on login link
    public void clickOnLogin() {
    	customWait.until(ExpectedConditions.elementToBeClickable(userdd)).click();
    	customWait.until(ExpectedConditions.elementToBeClickable(login)).click();
    }
}


