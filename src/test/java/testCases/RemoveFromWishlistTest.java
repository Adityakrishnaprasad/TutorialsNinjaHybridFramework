package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.WishListPage;
import testBase.BaseClass;
import utilities.LoggerLoad; 
public class RemoveFromWishlistTest extends BaseClass {  

    WishListPage wlp;

    @Test(dependsOnGroups = "addToCart")
    public void removeFromWishlist() {
        LoggerLoad.info("===== Starting test: removeFromWishlist =====");

        wlp = new WishListPage(getDriver());

        LoggerLoad.info("Step 1: Remove first product from wishlist");
        wlp.removeFirstProduct();

        LoggerLoad.info("Step 2: Verify wishlist is empty");
        String actualMessage = wlp.getEmptyWishlistMessage();
        String expectedMessage = "Your wish list is empty.";

        Assert.assertEquals(actualMessage, expectedMessage, 
                "Wishlist is not empty after removing the product.");
        LoggerLoad.info("Assertion Passed: Wishlist is empty. Message displayed: " + actualMessage);

        LoggerLoad.info("===== Finished test: removeFromWishlist =====");
    }
}
