package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.wishListPage;
import testBase.baseClass;

public class removeFromWishlist extends baseClass{  

    wishListPage wlp;

    @Test
    public void RemoveFromWishList() {
        wlp = new wishListPage(driver);
        wlp.clickOnFirstRemoveIcon();
        String actualMessage = wlp.getEmptyWishlistMessage();
        String expectedMessage = "Your wish list is empty.";
        Assert.assertEquals(actualMessage, expectedMessage, "Wishlist is not empty after removing the product.");
    }

}
