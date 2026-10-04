package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // Product title on the product page
    @FindBy(xpath = "//div[@id='content']//h1")
    private WebElement productTitle;

    @FindBy(xpath = "//div[@id='content']//h1/following::h2[1]")
    private WebElement priceField;

    @FindBy(xpath = "//button[@type='button']//i[@class='fa fa-heart']")
    private WebElement addToWishlistButton;

    @FindBy(xpath = "//div[@class='alert alert-success alert-dismissible']")
    private WebElement alertMessage;

    @FindBy(xpath = "//a[contains(@title,'Wish List')] /child::span")
    private WebElement wishlistHeaderLink;

    public String getProductName() {
        LoggerLoad.info("Fetching product name");
        customWait.until(ExpectedConditions.visibilityOf(productTitle));
        String name = productTitle.getText().trim();
        LoggerLoad.info("Product name found: " + name);
        return name;
    }

    /** 
     * @return String
     */
    public String getPrice() {
        LoggerLoad.info("Fetching product price");
        customWait.until(ExpectedConditions.visibilityOf(priceField));
        String price = priceField.getText().trim();
        LoggerLoad.info("Product price found: " + price);
        return price;
    }

    public void clickAddToWishlist() {
        LoggerLoad.info("Clicking on Wishlist icon for product");
        click(addToWishlistButton);
    }

    public String getAlertMessage() {
        LoggerLoad.info("Reading alert message");
        customWait.until(ExpectedConditions.visibilityOf(alertMessage));
        String msg = alertMessage.getText().trim();
        LoggerLoad.info("Alert message: " + msg);
        return msg;
    }

    public void openWishlist() {
        LoggerLoad.info("Clicking on Wishlist (top navigation) link");
        click(wishlistHeaderLink);
    }
}
