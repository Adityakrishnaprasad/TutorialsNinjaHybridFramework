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

	
	@FindBy(xpath="//h1[text()='Samsung Galaxy Tab 10.1']") WebElement prodName;
	@FindBy(xpath="//h1[normalize-space()='Samsung Galaxy Tab 10.1']/following::h2[1]") WebElement priceField;
	@FindBy(xpath="//button[@type='button']//i[@class='fa fa-heart']") WebElement wishlist;
	@FindBy(xpath="//div[@class='alert alert-success alert-dismissible']") WebElement wishlistAlertMessage;
	
	
	
	
	public void verifyText() {
		Assert.assertEquals(prodName.getText(),myAccountPage.prod.getText(),"else product is not matching");
	}
	
	public void GetPrize() {
		System.out.println(priceField.getText());
	}
	
	public void clickWishlistAndVerifyAlert() {
	    ww.until(ExpectedConditions.elementToBeClickable(wishlist)).click();
	    WebElement alert = ww.until(ExpectedConditions.visibilityOf(wishlistAlertMessage));
	    Assert.assertTrue(alert.isDisplayed(), "Alert message is not displayed");
	}
	
	

}
