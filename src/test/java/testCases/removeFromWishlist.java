package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.wishListPage;
import testBase.baseClass;
import utilities.LoggerLoad; 
public class removeFromWishlist extends baseClass {  

    wishListPage wlp;

    @Test
    public void RemoveFromWishList() {
        LoggerLoad.info("===== Starting test: RemoveFromWishList =====");

        wlp = new wishListPage(getDriver());

        LoggerLoad.info("Step 1: Remove first product from wishlist");
        wlp.clickOnFirstRemoveIcon();

        LoggerLoad.info("Step 2: Verify wishlist is empty");
        String actualMessage = wlp.getEmptyWishlistMessage();
        String expectedMessage = "Your wish list is empty.";

        Assert.assertEquals(actualMessage, expectedMessage, 
                "Wishlist is not empty after removing the product.");
        LoggerLoad.info("Assertion Passed: Wishlist is empty. Message displayed: " + actualMessage);

        LoggerLoad.info("===== Finished test: RemoveFromWishList =====");
    }
}
