package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import utilities.LoggerLoad; // <-- import logger

public class wishListPage extends basePage {

    public wishListPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath="(//tbody/tr/td[2]/a)") 
    private WebElement productName;

    @FindBy(xpath="//tbody/tr/td[5]/div") 
    private WebElement productPrice;

    @FindBy(xpath="//button[@data-original-title='Add to Cart']/i") 
    private WebElement addToCartBtn;

    @FindBy(css="div[class='alert alert-success alert-dismissible']") 
    private WebElement confirmationPopup;

    @FindBy(xpath="//button[@class='btn btn-inverse btn-block btn-lg dropdown-toggle']") 
    private WebElement cart2;

    @FindBy(xpath="//button/i[@class='fa fa-times']") 
    private WebElement closeicon;

    @FindBy(xpath="//i[@class='fa fa-times']") 
    private WebElement removeIcon;

    @FindBy(xpath="//p[text()='Your wish list is empty.']") 
    private WebElement emptyWishlistMessage;    

    public void clickOnFirstAddtoCartBtn() {
    	System.out.println();
        LoggerLoad.info("Clicking on 'Add to Cart' button for first product in wishlist");
        try {
            customWait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
        } catch (Exception e) {
            LoggerLoad.warn("Standard click failed, using JavaScript click for 'Add to Cart' button");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", addToCartBtn
            );
        }
    }

    public void VerifyPopUp() {
        LoggerLoad.info("Verifying confirmation popup after adding to cart");
        customWait.until(ExpectedConditions.visibilityOf(confirmationPopup));
        Assert.assertTrue(confirmationPopup.isDisplayed(), "Confirmation popup is not displayed");
        LoggerLoad.info("Confirmation popup displayed successfully");
    }

    public void clickOnCart2() {
        LoggerLoad.info("Clicking on Cart dropdown (top navigation)");
        customWait.until(ExpectedConditions.elementToBeClickable(cart2)).click();
    }

    public void closeIcon() {
        LoggerLoad.info("Clicking on Close icon inside cart");
        customWait.until(ExpectedConditions.elementToBeClickable(closeicon)).click();
    }

    public String getProductName() {
        LoggerLoad.info("Fetching product name from wishlist");
        customWait.until(ExpectedConditions.visibilityOf(productName));
        String name = productName.getText();
        LoggerLoad.info("Product name in wishlist: " + name);
        return name;
    }

    public String getProductPrice() {
        LoggerLoad.info("Fetching product price from wishlist");
        customWait.until(ExpectedConditions.visibilityOf(productPrice));
        String price = productPrice.getText();
        LoggerLoad.info("Product price in wishlist: " + price);
        return price;
    }

    public void clickOnFirstRemoveIcon() {
        LoggerLoad.info("Clicking on Remove icon for first product in wishlist");
        try {
            customWait.until(ExpectedConditions.elementToBeClickable(removeIcon)).click();
        } catch (Exception e) {
            LoggerLoad.warn("Standard click failed, using JavaScript click for Remove icon");
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", removeIcon
            );
        }
    }

    public String getEmptyWishlistMessage() {
        LoggerLoad.info("Fetching empty wishlist message");
        customWait.until(ExpectedConditions.visibilityOf(emptyWishlistMessage));
        String msg = emptyWishlistMessage.getText();
        LoggerLoad.info("Empty wishlist message: " + msg);
        System.out.println();
        return msg;
    }   
}
