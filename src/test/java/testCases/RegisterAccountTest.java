package testCases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import pageObjects.LandingPage;
import pageObjects.RegisterPage;
import testBase.BaseClass;
import utilities.DataGenerator;
import utilities.LoggerLoad;

public class RegisterAccountTest extends BaseClass {
    
    LandingPage lp;
    RegisterPage rp;

    @Test(groups = "register")
    public void registerNewAccount(ITestContext context) {
        LoggerLoad.info("===== Starting test: registerNewAccount =====");

        lp = new LandingPage(getDriver());
        LoggerLoad.info("Step 1: Navigate to Register page");
        lp.goToRegisterPage();

        String pwd = DataGenerator.getPassword();
        String email = DataGenerator.getEmail();

        rp = new RegisterPage(getDriver());
        LoggerLoad.info("Step 2: Fill out registration form with random test data");
        rp.enterFirstName(DataGenerator.getFirstName());
        rp.enterLastName(DataGenerator.getLastName());
        rp.enterEmail(email);
        rp.enterTelephone(DataGenerator.getTelephone());
        rp.enterPassword(pwd);
        rp.enterConfirmPassword(pwd);
        rp.acceptPrivacyPolicy();

        LoggerLoad.info("Step 3: Submit registration form");
        rp.clickContinue();

        LoggerLoad.info("Step 4: Verify account creation success");
        Assert.assertEquals(rp.getConfirmationText(), "Your Account Has Been Created!", "Account was not created");

        // Save this browser's new account so LoginTest can use it
        context.setAttribute(USER_EMAIL, email);
        context.setAttribute(USER_PASSWORD, pwd);
        LoggerLoad.info("Saved new account for this browser: " + email);

        LoggerLoad.info("===== Finished test: registerNewAccount =====");
    }
}
