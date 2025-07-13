package testCases;

import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.loginPage;
import testBase.baseClass;

public class loginTest extends baseClass{
	landingPage lp;
	loginPage logP;
	@Test
	public void loginTest() {
		lp = new landingPage(driver);
		lp.clickOnUserdd();
		lp.clickOnLogin();
		
		logP = new loginPage(driver);
		logP.enterEmail("trendsetter69@gmail.com");
		logP.enterPassword("Trendsetter@69");
		logP.clickLoginButton();
		
		logP.verifyLoginSuccess();
	}
}
