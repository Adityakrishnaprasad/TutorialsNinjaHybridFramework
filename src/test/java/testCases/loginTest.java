package testCases;

import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.loginPage;
import testBase.baseClass;
import utilities.configurationReader;
import utilities.LoggerLoad; // <-- import logger

public class loginTest extends baseClass {

    landingPage lp;
    loginPage logP;
    String user = configurationReader.get("app_username");
    String pass = configurationReader.get("app_password");

    @Test
    public void loginTestApp() {
        LoggerLoad.info("===== Starting test: loginTestApp =====");
        LoggerLoad.info("Step 1: Navigate to Login page");

        lp = new landingPage(driver);
        lp.clickOnLogin();

        LoggerLoad.info("Step 2: Enter login credentials");
        logP = new loginPage(driver);
        logP.enterEmail(user);
        logP.enterPassword(pass);

        LoggerLoad.info("Step 3: Submit login form");
        logP.clickLoginButton();

        LoggerLoad.info("Step 4: Verify login success");
        logP.verifyLoginSuccess();

        LoggerLoad.info("===== Finished test: loginTestApp =====");
    }
}
