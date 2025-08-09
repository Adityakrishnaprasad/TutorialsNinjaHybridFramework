package testCases;

import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import pageObjects.productListingPage;
import testBase.baseClass;

public class searchAndAddtoWishlistaProduct extends baseClass{
	myAccountPage map;
	productListingPage plp;
	@Test
	public void searchandaddtowishlist() throws InterruptedException {
		map = new myAccountPage(driver);
		String productToSearch = "Samsung Galaxy Tab 10.1";
		map.SearchForProduct(productToSearch);
		map.clickOnProduct();
		
		plp = new productListingPage(driver);
		plp.verifyText();
		plp.getPrice();
		plp.clickWishlistAndVerifyAlert();
		plp.clickOnWishlist2();
		
	}

}
