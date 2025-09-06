package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import utilities.LoggerLoad; // <-- import logger

public class productListingPage extends basePage {

    public productListingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[text()='Samsung Galaxy Tab 10.1']")
    private WebElement prodName;

    @FindBy(xpath = "//h1[normalize-space()='Samsung Galaxy Tab 10.1']/following::h2[1]")
    private WebElement priceField;

    @FindBy(xpath = "//button[@type='button']//i[@class='fa fa-heart']")
    private WebElement wishlist;

    @FindBy(xpath = "//div[@class='alert alert-success alert-dismissible']")
    private WebElement wishlistAlertMessage;

    @FindBy(xpath = "//a[contains(@title,'Wish List')] /child::span")
    private WebElement wishlist2;

    public void verifyText() {
    	System.out.println();
        LoggerLoad.info("Verifying product name on Product Listing page");
        customWait.until(ExpectedConditions.visibilityOf(prodName));
        Assert.assertEquals(prodName.getText().trim(), "Samsung Galaxy Tab 10.1",
                "Product name is not matching!");
        LoggerLoad.info("Product name verified successfully: " + prodName.getText().trim());
    }

    public String getPrice() {
        LoggerLoad.info("Fetching product price");
        customWait.until(ExpectedConditions.visibilityOf(priceField));
        String price = priceField.getText().trim();
        LoggerLoad.info("Product price found: " + price);
        return price;
    }

    public void assertPrice(String expectedPrice) {
        LoggerLoad.info("Asserting product price. Expected: " + expectedPrice);
        Assert.assertEquals(getPrice(), expectedPrice, "Product price does not match");
        LoggerLoad.info("Product price verified successfully");
    }

    public void clickWishlistAndVerifyAlert() {
        LoggerLoad.info("Clicking on Wishlist icon for product");
        try {
            customWait.until(ExpectedConditions.elementToBeClickable(wishlist)).click();
        } catch (Exception e) {
            LoggerLoad.warn("Standard click failed, using JavaScript click for Wishlist icon");
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", wishlist);
        }

        LoggerLoad.info("Waiting for Wishlist alert message to appear");
        customWait.until(ExpectedConditions.visibilityOf(wishlistAlertMessage));
        Assert.assertTrue(wishlistAlertMessage.isDisplayed(),
                "Wishlist alert message is not displayed");
        LoggerLoad.info("Wishlist alert message displayed successfully");
    }

    public void clickOnWishlist2() {
        LoggerLoad.info("Clicking on Wishlist (top navigation) link");
        try {
            customWait.until(ExpectedConditions.elementToBeClickable(wishlist2)).click();
        } catch (Exception e) {
            LoggerLoad.warn("Standard click failed, using JavaScript click for Wishlist (top nav)");
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", wishlist2);
        }
        System.out.println();
    }
}
