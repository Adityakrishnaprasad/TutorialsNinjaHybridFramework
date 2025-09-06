package testCases;

import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import pageObjects.productListingPage;
import testBase.baseClass;
import utilities.LoggerLoad; // <-- import logger

public class searchAndAddtoWishlistaProduct extends baseClass {
    
    myAccountPage map;
    productListingPage plp;

    @Test
    public void searchandaddtowishlist() throws InterruptedException {
        LoggerLoad.info("===== Starting test: searchandaddtowishlist =====");

        map = new myAccountPage(driver);
        String productToSearch = "Samsung Galaxy Tab 10.1";

        LoggerLoad.info("Step 1: Search for product: " + productToSearch);
        map.SearchForProduct(productToSearch);

        LoggerLoad.info("Step 2: Click on product from search results");
        map.clickOnProduct();

        plp = new productListingPage(driver);

        LoggerLoad.info("Step 3: Verify product details");
        plp.verifyText();
        String price = plp.getPrice();
        LoggerLoad.info("Product price retrieved: " + price);

        LoggerLoad.info("Step 4: Add product to wishlist and verify alert");
        plp.clickWishlistAndVerifyAlert();

        LoggerLoad.info("Step 5: Open wishlist page from navigation bar");
        plp.clickOnWishlist2();

        LoggerLoad.info("===== Finished test: searchandaddtowishlist =====");
    }
}
