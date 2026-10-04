package testCases;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import pageObjects.EditAccountPage;
import testBase.BaseClass;
import utilities.DataGenerator;
import utilities.LoggerLoad;

public class EditAccountTest extends BaseClass {
	
	String msg = "Success: Your account has been successfully updated.";
	
	EditAccountPage editAcc;

	@Test(groups = "editAccount", dependsOnGroups = "register")
	public void editAccountDetails(ITestContext context) {

		LoggerLoad.info("===== Starting test: editAccountDetails =====");

		editAcc = new EditAccountPage(getDriver());

		LoggerLoad.info("Step 1-2: Go to My Account page");
		editAcc.goToMyAccountPage();

		LoggerLoad.info("Step 3: Navigating to Edit Account page");
		editAcc.goToEditAccountPage();

		LoggerLoad.info("Step 4: Updating First Name");
		editAcc.enterFirstName(DataGenerator.getFirstName());

		LoggerLoad.info("Step 5: Updating Last Name");
		editAcc.enterLastName(DataGenerator.getLastName());

		LoggerLoad.info("Step 6: Updating Email");
		String newEmail = DataGenerator.getEmail();
		editAcc.enterEmail(newEmail);

		LoggerLoad.info("Step 7: Updating Telephone");
		editAcc.enterTelephone(DataGenerator.getTelephone());

		LoggerLoad.info("Step 8: Clicking Continue button");
		editAcc.clickContinue();

		LoggerLoad.info("Step 9: Verifying success message");
		String msgConfirmation = editAcc.getSuccessMessage();

		Assert.assertEquals(msgConfirmation, msg);

		// Email changed, so update this browser's saved account for LoginTest
		context.setAttribute(USER_EMAIL, newEmail);
		LoggerLoad.info("Updated saved account email: " + newEmail);

		LoggerLoad.info("Account details updated successfully");
		LoggerLoad.info("===== Finished test: editAccountDetails =====");
	}
}