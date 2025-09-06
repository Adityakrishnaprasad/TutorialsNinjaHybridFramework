package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad; 

public class landingPage extends basePage {

    public landingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[@title='My Account']")
    private WebElement userdd;

    @FindBy(xpath = "//a[normalize-space()='Register']")
    private WebElement regLink;

    @FindBy(xpath = "//a[normalize-space()='Login']")
    private WebElement login;

    // clicks on user dropdown and clicks on register user link
    public void clickOnuserReg() {
    	System.out.println();
        LoggerLoad.info("Clicking on 'My Account' dropdown");
        customWait.until(ExpectedConditions.elementToBeClickable(userdd)).click();

        LoggerLoad.info("Clicking on 'Register' link");
        customWait.until(ExpectedConditions.elementToBeClickable(regLink)).click();
    }

    // clicks on user dropdown and clicks on login link
    public void clickOnLogin() {
        LoggerLoad.info("Clicking on 'My Account' dropdown");
        customWait.until(ExpectedConditions.elementToBeClickable(userdd)).click();

        LoggerLoad.info("Clicking on 'Login' link");
        customWait.until(ExpectedConditions.elementToBeClickable(login)).click();
        System.out.println();
    }
}
