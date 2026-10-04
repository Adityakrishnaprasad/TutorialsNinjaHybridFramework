package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.WishListPage;
import testBase.BaseClass;
import utilities.LoggerLoad; 

public class AddToCartFromWishlistTest extends BaseClass {
    
    WishListPage wlp;

    @Test(groups = "addToCart", dependsOnGroups = "searchWishlist")
    public void addToCartFromWishlist() {
    	
        LoggerLoad.info("===== Starting test: addToCartFromWishlist =====");

        wlp = new WishListPage(getDriver());

        LoggerLoad.info("Step 1: Add the first wishlist product to the cart");
        wlp.addFirstProductToCart();

        LoggerLoad.info("Step 2: Verify the product was added to the cart");
        String msg = wlp.getConfirmationMessage();
        Assert.assertTrue(msg.contains("Success: You have added"), "Add to cart success message not shown. Actual: " + msg);
        Assert.assertTrue(msg.contains("Samsung Galaxy Tab 10.1"), "Wrong product added to cart. Actual: " + msg);

        LoggerLoad.info("Step 3: Opening cart dropdown");
        wlp.openCartDropdown();

        LoggerLoad.info("Step 4: Remove the product from the cart (clean-up)");
        wlp.removeItemFromCart();

        LoggerLoad.info("===== Finished test: addToCartFromWishlist =====");
    }
}