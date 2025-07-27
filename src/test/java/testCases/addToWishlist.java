package testCases;

import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import pageObjects.productListingPage;
import pageObjects.wishListPage;
import testBase.baseClass;

public class addToWishlist extends baseClass{
	wishListPage wlp;
	
	@Test
	public void AddtoWishList() {
	 wlp = new wishListPage(driver);
	 wlp.clickOnAddtoCartBtn();
	 wlp.VerifyPopUp();
	 wlp.clickoncart2();
	 wlp.closeIcon();
		
	}

}
