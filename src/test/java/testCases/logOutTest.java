package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.myAccountPage;
import testBase.baseClass;

public class logOutTest extends baseClass{
	
	@Test
	public void logOutTest() {
		
		myAccountPage myAccPage = new myAccountPage(driver);
		myAccPage.clickOnMyAccountDropdown();
		myAccPage.clickOnLogout();
		
		// Verify logout by checking if the continue button is displayed
		myAccPage.clickOnContinue();
		
		
	}
}


