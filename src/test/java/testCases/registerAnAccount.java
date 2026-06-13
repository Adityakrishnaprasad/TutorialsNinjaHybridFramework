package testCases;

import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.registerPage;
import testBase.baseClass;
import utilities.DataGenerator;
import utilities.LoggerLoad; // <-- import logger

public class registerAnAccount extends baseClass {
    
    landingPage lp;
    registerPage rp;

    @Test
    public void CreateUser() {
        LoggerLoad.info("===== Starting test: CreateUser =====");

        lp = new landingPage(getDriver());
        LoggerLoad.info("Step 1: Navigate to Register page");
        lp.clickOnuserReg();

        String pwd = DataGenerator.getPassword();

        rp = new registerPage(getDriver());
        LoggerLoad.info("Step 2: Fill out registration form with random test data");
        rp.enterFirstName(DataGenerator.getFirstName());
        rp.enterLastName(DataGenerator.getLastName());
        rp.enterEmail(DataGenerator.getEmail());
        rp.enterTelephone(DataGenerator.getTelephone());
        rp.enterPassword(pwd);
        rp.enterConfirmPassword(pwd);
        rp.clickCheckbox();

        LoggerLoad.info("Step 3: Submit registration form");
        rp.clickContinueButton();

        LoggerLoad.info("Step 4: Verify account creation success");
        rp.verifyText();

        LoggerLoad.info("===== Finished test: CreateUser =====");
    }
}
