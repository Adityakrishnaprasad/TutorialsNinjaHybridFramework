package pageObjects;

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
    @FindBy(xpath="//span[.='Wish List (1)']") private WebElement wishlist2;

   
    public void verifyText() {
        Assert.assertEquals(prodName.getText(),"Samsung Galaxy Tab 10.1","else product is not matching");
    }

    // Highlight: Rename getPrize to getPrice, return price instead of asserting
    public String getPrice() {
        ww.until(ExpectedConditions.visibilityOf(priceField));
        return priceField.getText();
    }

    public void assertPrice(String expectedPrice) {
        Assert.assertEquals(getPrice(), expectedPrice, "Product price does not match");
    }

    public void clickWishlistAndVerifyAlert() {
        ww.until(ExpectedConditions.elementToBeClickable(wishlist)).click();
        WebElement alert = ww.until(ExpectedConditions.visibilityOf(wishlistAlertMessage));
        Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed");
    }
    
    public void clickOnWishlist2() {
        ww.until(ExpectedConditions.elementToBeClickable(wishlist2)).click();
    }
	
	

}
