package testCases;

import org.testng.annotations.Test;

import pageObjects.landingPage;
import pageObjects.registerPage;
import testBase.baseClass;
import utilities.DataGenerator;

public class registerAnAccount extends baseClass{
landingPage lp;
registerPage rp;


	@Test
	public void CreateUser() {
		lp = new landingPage(driver);
		lp.clickOnuserReg();
		
		String pwd = DataGenerator.getPassword();
		
		
		rp = new registerPage(driver);
		rp.enterFirstName(DataGenerator.getFirstName());
		rp.enterLastName(DataGenerator.getLastName());
		rp.enterEmail(DataGenerator.getEmail());
		rp.enterTelephone(DataGenerator.getTelephone());
		rp.enterPassword(pwd);
		rp.enterConfirmPassword(pwd);
		rp.clickCheckbox();
		rp.clickContinueButton();
		rp.verifyText();
	}
}
