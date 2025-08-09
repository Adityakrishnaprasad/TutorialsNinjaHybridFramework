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
	
	
	@FindBy(xpath="(//tbody/tr/td[2]/a)") private WebElement productName;
    @FindBy(xpath="//tbody/tr/td[5]/div") private WebElement productPrice;
    @FindBy(xpath="//button[@data-original-title='Add to Cart']/i") private WebElement addtoCartBtn;
    @FindBy(css="div[class='alert alert-success alert-dismissible']") private WebElement confirmationPopup;
    @FindBy(xpath="//button[@class='btn btn-inverse btn-block btn-lg dropdown-toggle']") private WebElement cart2;
    @FindBy(xpath="//button/i[@class='fa fa-times']") private WebElement closeicon;

    public void clickOnAddtoCartBtn() {
        ww.until(ExpectedConditions.elementToBeClickable(addtoCartBtn)).click();
    }

    public void VerifyPopUp() {
        // Highlight: Wait for popup visibility before assertion
        ww.until(ExpectedConditions.visibilityOf(confirmationPopup));
        Assert.assertTrue(confirmationPopup.isDisplayed(), "Confirmation popup is not displayed");
    }

    // Highlight: Rename clickoncart2 to clickOnCart2 for naming consistency
    public void clickOnCart2() {
        ww.until(ExpectedConditions.elementToBeClickable(cart2)).click();
    }

    public void closeIcon() {
        ww.until(ExpectedConditions.elementToBeClickable(closeicon)).click();
    }

    // Highlight: Add getter methods for product name and price
    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }
    
    
}
