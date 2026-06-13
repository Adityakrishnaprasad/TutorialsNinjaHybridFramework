package testCases;

import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import testBase.baseClass;
import utilities.LoggerLoad; 

public class logOutTest extends baseClass {
    
    @Test
    public void logOutTestapp() {
        LoggerLoad.info("===== Starting test: logOutTestapp =====");

        myAccountPage myAccPage = new myAccountPage(getDriver());

        LoggerLoad.info("Step 1: Open 'My Account' dropdown");
        myAccPage.clickOnMyAccountDropdown();

        LoggerLoad.info("Step 2: Click on 'Logout'");
        myAccPage.clickOnLogout();

        LoggerLoad.info("Step 3: Verify logout by clicking on 'Continue'");
        myAccPage.clickOnContinue();

        LoggerLoad.info("===== Finished test: logOutTestapp =====");
    }
}
