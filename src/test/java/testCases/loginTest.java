package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.loginPage;
import testBase.baseClass;
import utilities.LoggerLoad;
import utilities.configurationReader; 

public class loginTest extends baseClass {

    landingPage lp;
    loginPage logP;

    @Test(groups = "login", dependsOnGroups = "logout")
    public void loginTestApp(ITestContext context) {
        LoggerLoad.info("===== Starting test: loginTestApp =====");

        // Use this browser's account from the current run; fall back to .env if none was saved
        String user = (String) context.getAttribute(USER_EMAIL);
        String pass = (String) context.getAttribute(USER_PASSWORD);
        if (user == null || pass == null) {
            user = configurationReader.get("app_username");
            pass = configurationReader.get("app_password");
            LoggerLoad.info("No account saved in this run, using credentials from .env");
        } else {
            LoggerLoad.info("Using account created in this run: " + user);
        }

        LoggerLoad.info("Step 1: Navigate to Login page");

        lp = new landingPage(getDriver());
        lp.clickOnLogin();

        LoggerLoad.info("Step 2: Enter login credentials");
        logP = new loginPage(getDriver());
        logP.enterEmail(user);
        logP.enterPassword(pass);

        LoggerLoad.info("Step 3: Submit login form");
        logP.clickLoginButton();

        LoggerLoad.info("Step 4: Verify login success");
        logP.verifyLoginSuccess();

        LoggerLoad.info("===== Finished test: loginTestApp =====");
    }
}
