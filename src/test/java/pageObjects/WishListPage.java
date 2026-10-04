package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad;

public class WishListPage extends BasePage {

    public WishListPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath="//div[@id='content']//tbody/tr/td[2]/a") 
    private WebElement productName;

    @FindBy(xpath="//div[@id='content']//tbody/tr/td[5]/div") 
    private WebElement productPrice;

    @FindBy(xpath="//button[@data-original-title='Add to Cart']/i") 
    private WebElement addToCartButton;

    @FindBy(css="div[class='alert alert-success alert-dismissible']") 
    private WebElement confirmationPopup;

    @FindBy(xpath="//button[@class='btn btn-inverse btn-block btn-lg dropdown-toggle']") 
    private WebElement cartButton;

    @FindBy(xpath="//button/i[@class='fa fa-times']") 
    private WebElement cartRemoveIcon;

    @FindBy(xpath="//div[@id='content']//a[contains(@href,'remove=')]") 
    private WebElement removeIcon;

    @FindBy(xpath="//p[text()='Your wish list is empty.']") 
    private WebElement emptyWishlistMessage;    

    public void addFirstProductToCart() {
        LoggerLoad.info("Clicking on 'Add to Cart' button for first product in wishlist");
        click(addToCartButton);
    }

    public String getConfirmationMessage() {
        LoggerLoad.info("Reading confirmation message after adding to cart");
        customWait.until(ExpectedConditions.visibilityOf(confirmationPopup));
        String msg = confirmationPopup.getText().trim();
        LoggerLoad.info("Confirmation message: " + msg);
        return msg;
    }

    public void openCartDropdown() {
        LoggerLoad.info("Clicking on Cart dropdown (top navigation)");
        click(cartButton);
    }

    public void removeItemFromCart() {
        LoggerLoad.info("Clicking on Close icon inside cart");
        click(cartRemoveIcon);
    }

    /** 
     * @return String
     */
    public String getProductName() {
        LoggerLoad.info("Fetching product name from wishlist");
        customWait.until(ExpectedConditions.visibilityOf(productName));
        String name = productName.getText();
        LoggerLoad.info("Product name in wishlist: " + name);
        return name;
    }

    /** 
     * @return String
     */
    public String getProductPrice() {
        LoggerLoad.info("Fetching product price from wishlist");
        customWait.until(ExpectedConditions.visibilityOf(productPrice));
        String price = productPrice.getText();
        LoggerLoad.info("Product price in wishlist: " + price);
        return price;
    }

    public void removeFirstProduct() {
        LoggerLoad.info("Clicking on Remove icon for first product in wishlist");
        click(removeIcon);
    }

    /** 
     * @return String
     */
    public String getEmptyWishlistMessage() {
        LoggerLoad.info("Fetching empty wishlist message");
        customWait.until(ExpectedConditions.visibilityOf(emptyWishlistMessage));
        String msg = emptyWishlistMessage.getText();
        LoggerLoad.info("Empty wishlist message: " + msg);
        return msg;
    }   
}
