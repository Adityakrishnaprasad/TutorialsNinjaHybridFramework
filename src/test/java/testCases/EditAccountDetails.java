package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.editAccountPage;
import pageObjects.landingPage;
import pageObjects.registerPage;
import testBase.baseClass;
import utilities.DataGenerator;
import utilities.LoggerLoad;

public class EditAccountDetails extends baseClass {
	
	String msg = "Success: Your account has been successfully updated.";
	
	editAccountPage editAcc;
	landingPage lp;
	registerPage reg;

	@Test
	public void editAccountDetails() {

		LoggerLoad.info("===== Starting test: editAccountDetails =====");

		lp = new landingPage(getDriver());
		reg = new registerPage(getDriver());
		editAcc = new editAccountPage(getDriver());

		LoggerLoad.info("Step 1: Clicking on My Account");
		lp.clickonMyAccount();

		LoggerLoad.info("Step 2: Navigating to My Account page");
		editAcc.navigateToMyAccount();

		LoggerLoad.info("Step 3: Navigating to Edit Account page");
		editAcc.navigateToEditAccountPage();

		LoggerLoad.info("Step 4: Updating First Name");
		reg.enterFirstName(DataGenerator.getFirstName());

		LoggerLoad.info("Step 5: Updating Last Name");
		reg.enterLastName(DataGenerator.getLastName());

		LoggerLoad.info("Step 6: Updating Email");
		reg.enterEmail(DataGenerator.getEmail());

		LoggerLoad.info("Step 7: Updating Telephone");
		reg.enterTelephone(DataGenerator.getTelephone());

		LoggerLoad.info("Step 8: Clicking Continue button");
		reg.clickContinueButton();

		LoggerLoad.info("Step 9: Verifying success message");
		String msgConfirmation = editAcc.getSuccessMessage();

		Assert.assertEquals(msgConfirmation, msg);

		LoggerLoad.info("Account details updated successfully");
		LoggerLoad.info("===== Finished test: editAccountDetails =====");
	}
}