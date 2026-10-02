package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.registerPage;
import testBase.baseClass;
import utilities.DataGenerator;
import utilities.LoggerLoad; // <-- import logger

public class registerAnAccount extends baseClass {
    
    landingPage lp;
    registerPage rp;

    @Test(groups = "register")
    public void CreateUser(ITestContext context) {
        LoggerLoad.info("===== Starting test: CreateUser =====");

        lp = new landingPage(getDriver());
        LoggerLoad.info("Step 1: Navigate to Register page");
        lp.clickOnuserReg();

        String pwd = DataGenerator.getPassword();
        String email = DataGenerator.getEmail();

        rp = new registerPage(getDriver());
        LoggerLoad.info("Step 2: Fill out registration form with random test data");
        rp.enterFirstName(DataGenerator.getFirstName());
        rp.enterLastName(DataGenerator.getLastName());
        rp.enterEmail(email);
        rp.enterTelephone(DataGenerator.getTelephone());
        rp.enterPassword(pwd);
        rp.enterConfirmPassword(pwd);
        rp.clickCheckbox();

        LoggerLoad.info("Step 3: Submit registration form");
        rp.clickContinueButton();

        LoggerLoad.info("Step 4: Verify account creation success");
        rp.verifyText();

        // Save this browser's new account so loginTest can use it
        context.setAttribute(USER_EMAIL, email);
        context.setAttribute(USER_PASSWORD, pwd);
        LoggerLoad.info("Saved new account for this browser: " + email);

        LoggerLoad.info("===== Finished test: CreateUser =====");
    }
}
