package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class wishListPage extends basePage{

	public wishListPage(WebDriver driver) {
		super(driver);
	}
	
	
	@FindBy(xpath="(//tbody/tr/td[2]/a") WebElement ProductName;
	@FindBy(xpath="//tbody/tr/td[5]/div") WebElement ProductPrice;
	@FindBy(xpath="//button[@data-original-title='Add to Cart']/i") WebElement AddtoCartBtn;
	@FindBy(css="div[class='alert alert-success alert-dismissible']") WebElement confirmationPopup;
	@FindBy(xpath="//button[@class='btn btn-inverse btn-block btn-lg dropdown-toggle']") WebElement cart2;
	@FindBy(xpath="//button/i[@class='fa fa-times']") WebElement closeicon;
	
	
	public void clickOnAddtoCartBtn() {
		AddtoCartBtn.click();
	}
	
    public void VerifyPopUp() {
    	
    	Assert.assertTrue(confirmationPopup.isDisplayed(), null);
    }
    
    public void clickoncart2() {
    	ww.until(ExpectedConditions.elementToBeClickable(cart2)).click();
    }
    
    public void closeIcon() {
    	ww.until(ExpectedConditions.elementToBeClickable(closeicon)).click();
    	
    }
    
    
}
