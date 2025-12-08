package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad; 

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

    // Direct product link
    @FindBy(xpath="//h4/a[text()='Samsung Galaxy Tab 10.1']") 
    private WebElement prod;

    @FindBy(xpath="//i[@class='fa fa-search']") 
    private WebElement searchBtn;

    public void clickOnMyAccountDropdown() {
    	System.out.println();
        LoggerLoad.info("Clicking on 'My Account' dropdown");
        customWait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown)).click();  
    }

    public void clickOnLogout() {
        LoggerLoad.info("Clicking on 'Logout' link");
        customWait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();  
    }

    public void clickOnContinue() {
        LoggerLoad.info("Clicking on 'Continue' button after logout");
        customWait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    /** 
     * @param pName
     */
    public void SearchForProduct(String pName) {
        LoggerLoad.info("Searching for product: " + pName);
        customWait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();   
        searchField.sendKeys(pName);
        customWait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();
    }

    public void clickOnProduct() {
        LoggerLoad.info("Clicking on product: Samsung Galaxy Tab 10.1");
        try {
            customWait.until(ExpectedConditions.visibilityOf(prod));
            customWait.until(ExpectedConditions.elementToBeClickable(prod)).click();
        } catch (Exception e) {
            LoggerLoad.warn("Standard click failed, trying JavaScript click on product");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", prod
            );
        }
    }
    
    /** 
     * @return String
     */
    public String getNameOfProduct() {
        LoggerLoad.info("Fetching product name");
        customWait.until(ExpectedConditions.visibilityOf(prod));
        String productName = prod.getText();
        LoggerLoad.info("Product name found: " + productName);
        System.out.println();
        return productName;
        
    }
}
