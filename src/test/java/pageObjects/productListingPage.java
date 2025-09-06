package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class productListingPage extends basePage {


	public productListingPage(WebDriver driver) {
		super(driver);
	}

	
	@FindBy(xpath="//h1[text()='Samsung Galaxy Tab 10.1']") private WebElement prodName;
    @FindBy(xpath="//h1[normalize-space()='Samsung Galaxy Tab 10.1']/following::h2[1]")  private WebElement priceField;
    @FindBy(xpath="//button[@type='button']//i[@class='fa fa-heart']") private WebElement wishlist;
    @FindBy(xpath="//div[@class='alert alert-success alert-dismissible']") private WebElement wishlistAlertMessage;
    @FindBy(xpath="//a[contains(@title,'Wish List')] /child::span") private WebElement wishlist2;

   
    public void verifyText() {
    	customWait.until(ExpectedConditions.visibilityOf(prodName));
          Assert.assertEquals(prodName.getText().trim(), "Samsung Galaxy Tab 10.1", "Product name is not matching!");
    }

    // Highlight: Rename getPrize to getPrice, return price instead of asserting
    public String getPrice() {
    	customWait.until(ExpectedConditions.visibilityOf(priceField));
         return priceField.getText().trim();
    }

    public void assertPrice(String expectedPrice) {
        Assert.assertEquals(getPrice(), expectedPrice, "Product price does not match");
    }

    public void clickWishlistAndVerifyAlert() {
    	try {
    		customWait.until(ExpectedConditions.elementToBeClickable(wishlist)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", wishlist
            );
        }
    	customWait.until(ExpectedConditions.visibilityOf(wishlistAlertMessage));
        Assert.assertTrue(wishlistAlertMessage.isDisplayed(), "Wishlist alert message is not displayed");
    }
    
    public void clickOnWishlist2() {
    	 try {
    		 customWait.until(ExpectedConditions.elementToBeClickable(wishlist2)).click();
         } catch (Exception e) {
             ((JavascriptExecutor) driver).executeScript(
                 "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", wishlist2
             );
         }
     }
    
 }

	
	

