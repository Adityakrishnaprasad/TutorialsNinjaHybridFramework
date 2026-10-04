package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad; 

public class RegisterPage extends BasePage {

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // locators
    @FindBy(css = "#input-firstname") private WebElement firstNameField;
    @FindBy(css = "#input-lastname") private WebElement lastNameField;
    @FindBy(css = "#input-email") private WebElement emailField;
    @FindBy(css = "#input-telephone") private WebElement telephoneField;
    @FindBy(css = "#input-password") private WebElement passwordField;
    @FindBy(css = "#input-confirm") private WebElement confirmPasswordField;
    @FindBy(xpath = "//input[@name='agree']") private WebElement privacyPolicyCheckbox;
    @FindBy(xpath = "//input[@value='Continue']") private WebElement continueButton;
    @FindBy(xpath = "//h1[text()='Your Account Has Been Created!']") private WebElement confirmationHeading;

    /** 
     * @param firstName
     */
    public void enterFirstName(String firstName) {
        LoggerLoad.info("Entering First Name: " + firstName);
        type(firstNameField, firstName);
    }

    /** 
     * @param lastName
     */
    public void enterLastName(String lastName) {
        LoggerLoad.info("Entering Last Name: " + lastName);
        type(lastNameField, lastName);
    }

    /** 
     * @param email
     */
    public void enterEmail(String email) {
        LoggerLoad.info("Entering Email: " + email);
        type(emailField, email);
    }

    /** 
     * @param telephone
     */
    public void enterTelephone(String telephone) {
        LoggerLoad.info("Entering Telephone: " + telephone);
        type(telephoneField, telephone);
    }

    /** 
     * @param password
     */
    public void enterPassword(String password) {
        LoggerLoad.info("Entering Password: [PROTECTED]");
        type(passwordField, password);
    }

    /** 
     * @param confirmPassword
     */
    public void enterConfirmPassword(String confirmPassword) {
        LoggerLoad.info("Entering Confirm Password: [PROTECTED]");
        type(confirmPasswordField, confirmPassword);
    }

    public void acceptPrivacyPolicy() {
        LoggerLoad.info("Clicking on 'Agree to Privacy Policy' checkbox");
        click(privacyPolicyCheckbox);
    }

    public void clickContinue() {
        LoggerLoad.info("Clicking on 'Continue' button to submit registration");
        click(continueButton);
    }

    public String getConfirmationText() {
        LoggerLoad.info("Reading account creation message");
        customWait.until(ExpectedConditions.visibilityOf(confirmationHeading));
        String text = confirmationHeading.getText().trim();
        LoggerLoad.info("Confirmation text: " + text);
        return text;
    }
}
