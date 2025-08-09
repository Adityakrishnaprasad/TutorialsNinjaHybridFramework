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
    

    
    public void clickOnUserdd() {
        ww.until(ExpectedConditions.elementToBeClickable(userdd));
        userdd.click();
    }

    public void clickOnuserReg() {
        ww.until(ExpectedConditions.elementToBeClickable(regLink));
        regLink.click();
    }

    public void clickOnLogin() {
        ww.until(ExpectedConditions.elementToBeClickable(login));
        login.click();
    }
}


