package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class myAccountPage extends basePage {

    public myAccountPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath="//a[@title='My Account']") 
    private WebElement myAccountDropdown;

    @FindBy(xpath="//a[text()='Logout']") 
    private WebElement logoutLink;

    @FindBy(xpath="//a[text()='Continue']") 
    private WebElement continueButton;

    @FindBy(name="search") 
    private WebElement searchField;

    //  Direct product link
    @FindBy(xpath="//h4/a[text()='Samsung Galaxy Tab 10.1']") 
    private WebElement prod;

    @FindBy(xpath="//i[@class='fa fa-search']") 
    private WebElement searchBtn;

    public void clickOnMyAccountDropdown() {
    	customWait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown)).click();  
    }

    public void clickOnLogout() {
    	customWait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();  
    }

    public void clickOnContinue() {
    	customWait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void SearchForProduct(String pName) {
    	customWait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();   
        searchField.sendKeys(pName);
        customWait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
    }

    public void clickOnProduct() {
        try {
        	customWait.until(ExpectedConditions.visibilityOf(prod));
        	customWait.until(ExpectedConditions.elementToBeClickable(prod)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", prod
            );
        }
    }
    
    public String getNameOfProduct() {
    	customWait.until(ExpectedConditions.visibilityOf(prod));
        return prod.getText();
    }
}
