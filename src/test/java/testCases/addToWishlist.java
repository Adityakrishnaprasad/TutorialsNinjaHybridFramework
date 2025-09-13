package testCases;

import org.testng.annotations.Test;

import pageObjects.wishListPage;
import testBase.baseClass;
import utilities.LoggerLoad; 

public class addToWishlist extends baseClass {
    
    wishListPage wlp;

    @Test
    public void AddtoWishList() {
    	
        LoggerLoad.info("===== Starting test: AddtoWishList =====");

        wlp = new wishListPage(getDriver());

        LoggerLoad.info("Step 1: Adding first product to wishlist");
        wlp.clickOnFirstAddtoCartBtn();

        LoggerLoad.info("Step 2: Verifying confirmation popup after adding to wishlist");
        wlp.VerifyPopUp();

        LoggerLoad.info("Step 3: Opening cart dropdown");
        wlp.clickOnCart2();

        LoggerLoad.info("Step 4: Closing cart dropdown");
        wlp.closeIcon();

        LoggerLoad.info("===== Finished test: AddtoWishList =====");
    }
}