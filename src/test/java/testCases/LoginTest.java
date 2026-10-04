package testCases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import pageObjects.LandingPage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.LoggerLoad;
import utilities.ConfigurationReader; 

public class LoginTest extends BaseClass {

    LandingPage lp;
    LoginPage logP;

    @Test(groups = "login", dependsOnGroups = "logout")
    public void login(ITestContext context) {
        LoggerLoad.info("===== Starting test: login =====");

        // Use this browser's account from the current run; fall back to .env if none was saved
        String user = (String) context.getAttribute(USER_EMAIL);
        String pass = (String) context.getAttribute(USER_PASSWORD);
        if (user == null || pass == null) {
            user = ConfigurationReader.getRequired("app_username");
            pass = ConfigurationReader.getRequired("app_password");
            LoggerLoad.info("No account saved in this run, using credentials from .env");
        } else {
            LoggerLoad.info("Using account created in this run: " + user);
        }

        LoggerLoad.info("Step 1: Navigate to Login page");

        lp = new LandingPage(getDriver());
        lp.goToLoginPage();

        LoggerLoad.info("Step 2: Enter login credentials");
        logP = new LoginPage(getDriver());
        logP.enterEmail(user);
        logP.enterPassword(pass);

        LoggerLoad.info("Step 3: Submit login form");
        logP.clickLoginButton();

        LoggerLoad.info("Step 4: Verify login success");
        Assert.assertTrue(logP.isMyAccountPageShown(), "Login failed: 'My Account' page was not shown");

        LoggerLoad.info("===== Finished test: login =====");
    }
}
