package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.MyAccountPage;
import pageObjects.ProductPage;
import pageObjects.WishListPage;
import testBase.BaseClass;
import utilities.LoggerLoad;

public class SearchAndAddToWishlistTest extends BaseClass {
    
    MyAccountPage map;
    ProductPage plp;

    @Test(groups = "searchWishlist", dependsOnGroups = "login")
    public void searchAndAddToWishlist() {
        LoggerLoad.info("===== Starting test: searchAndAddToWishlist =====");

        map = new MyAccountPage(getDriver());
        String productToSearch = "Samsung Galaxy Tab 10.1";

        LoggerLoad.info("Step 1: Search for product: " + productToSearch);
        map.searchForProduct(productToSearch);

        LoggerLoad.info("Step 2: Click on product from search results");
        map.clickOnProduct();

        plp = new ProductPage(getDriver());

        LoggerLoad.info("Step 3: Verify product details");
        Assert.assertEquals(plp.getProductName(), productToSearch, "Wrong product page opened");
        String price = plp.getPrice();
        LoggerLoad.info("Product price retrieved: " + price);

        LoggerLoad.info("Step 4: Add product to wishlist and verify alert");
        plp.clickAddToWishlist();
        String alert = plp.getAlertMessage();
        Assert.assertTrue(alert.contains("Success: You have added") && alert.contains("wish list"),
                "Product was not added to the wish list. Actual: " + alert);

        LoggerLoad.info("Step 5: Open wishlist page from navigation bar");
        plp.openWishlist();

        LoggerLoad.info("Step 6: Verify the wishlist has the same product and price");
        WishListPage wlp = new WishListPage(getDriver());
        Assert.assertEquals(wlp.getProductName().trim(), productToSearch, "Wrong product in wishlist");
        Assert.assertEquals(wlp.getProductPrice().trim(), price, "Wishlist price differs from product page");

        LoggerLoad.info("===== Finished test: searchAndAddToWishlist =====");
    }
}
