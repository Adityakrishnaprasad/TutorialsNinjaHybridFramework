package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.LoggerLoad; 

public class LogoutTest extends BaseClass {
    
    @Test(groups = "logout", dependsOnGroups = "editAccount")
    public void logout() {
        LoggerLoad.info("===== Starting test: logout =====");

        MyAccountPage myAccPage = new MyAccountPage(getDriver());

        LoggerLoad.info("Step 1: Open 'My Account' dropdown");
        myAccPage.openMyAccountMenu();

        LoggerLoad.info("Step 2: Click on 'Logout'");
        myAccPage.clickLogout();

        LoggerLoad.info("Step 3: Verify the logout page is shown");
        Assert.assertEquals(myAccPage.getPageHeading(), "Account Logout", "Logout page was not shown");

        LoggerLoad.info("Step 4: Click 'Continue' to return to the home page");
        myAccPage.clickContinue();

        LoggerLoad.info("===== Finished test: logout =====");
    }
}
