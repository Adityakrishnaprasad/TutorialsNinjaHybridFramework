package testCases;

import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.loginPage;
import testBase.baseClass;
import utilities.configurationReader;

public class loginTest extends baseClass{
	landingPage lp;
	loginPage logP;
	String user = configurationReader.get("app_username");
	String pass = configurationReader.get("app_password");

	@Test
	public void loginTestApp() {
		lp = new landingPage(driver);
		lp.clickOnUserdd();
		lp.clickOnLogin();
		
		logP = new loginPage(driver);
		logP.enterEmail(user);
		logP.enterPassword(pass);
		logP.clickLoginButton();
		
		logP.verifyLoginSuccess();
	}
}
