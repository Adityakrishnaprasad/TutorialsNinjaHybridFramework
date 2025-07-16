package testCases;

import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import pageObjects.productListingPage;
import testBase.baseClass;

public class searchAndAddtoWishlistaProduct extends baseClass{
	myAccountPage map;
	productListingPage plp;
	@Test
	public void searchandaddtowishlist() {
		map = new myAccountPage(driver);
		map.SearchForProduct();
		map.clickOnProduct();
		
		plp = new productListingPage(driver);
		plp.verifyText();
		plp.GetPrize();
		plp.clickWishlistAndVerifyAlert();
	}

}
