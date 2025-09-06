package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import utilities.LoggerLoad; 

public class registerPage extends basePage {

    public registerPage(WebDriver driver) {
        super(driver);
    }

    // locators
    @FindBy(css = "#input-firstname") private WebElement firstnameField;
    @FindBy(css = "#input-lastname") private WebElement lastnameField;
    @FindBy(css = "#input-email") private WebElement emailField;
    @FindBy(css = "#input-telephone") private WebElement telephoneField;
    @FindBy(css = "#input-password") private WebElement passwordField;
    @FindBy(css = "#input-confirm") private WebElement cnfpasswordField;
    @FindBy(xpath = "//input[@name='agree']") private WebElement checkboxField;
    @FindBy(xpath = "//input[@value='Continue']") private WebElement continueButton;
    @FindBy(xpath = "//h1[text()='Your Account Has Been Created!']") private WebElement confirmTxt;

    public void enterFirstName(String firstName) {
    	System.out.println();
        LoggerLoad.info("Entering First Name: " + firstName);
        firstnameField.clear();
        firstnameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        LoggerLoad.info("Entering Last Name: " + lastName);
        lastnameField.clear();
        lastnameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        LoggerLoad.info("Entering Email: " + email);
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterTelephone(String telephone) {
        LoggerLoad.info("Entering Telephone: " + telephone);
        telephoneField.clear();
        telephoneField.sendKeys(telephone);
    }

    public void enterPassword(String password) {
        LoggerLoad.info("Entering Password: [PROTECTED]");
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        LoggerLoad.info("Entering Confirm Password: [PROTECTED]");
        cnfpasswordField.clear();
        cnfpasswordField.sendKeys(confirmPassword);
    }

    public void clickCheckbox() {
        LoggerLoad.info("Clicking on 'Agree to Privacy Policy' checkbox");
        customWait.until(ExpectedConditions.elementToBeClickable(checkboxField)).click();
    }

    public void clickContinueButton() {
        LoggerLoad.info("Clicking on 'Continue' button to submit registration");
        customWait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void verifyText() {
        LoggerLoad.info("Verifying account creation success message");
        customWait.until(ExpectedConditions.visibilityOf(confirmTxt));
        String actTxt = confirmTxt.getText();
        Assert.assertEquals(actTxt, "Your Account Has Been Created!",
                "Account creation confirmation text mismatch!");
        LoggerLoad.info("Account created successfully. Confirmation text: " + actTxt);
        System.out.println();
    }
}
