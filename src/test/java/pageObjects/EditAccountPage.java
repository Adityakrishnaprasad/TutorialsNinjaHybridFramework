package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad;

public class EditAccountPage extends BasePage {

    public EditAccountPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[text()='Edit your account information']")
    private WebElement editAccountInfoLink;

    @FindBy(css = "#input-firstname") private WebElement firstNameField;
    @FindBy(css = "#input-lastname") private WebElement lastNameField;
    @FindBy(css = "#input-email") private WebElement emailField;
    @FindBy(css = "#input-telephone") private WebElement telephoneField;
    @FindBy(xpath = "//input[@value='Continue']") private WebElement continueButton;

    @FindBy(xpath = "//div[contains(@class,'alert-success')]")
    private WebElement successAlert;

    public void goToEditAccountPage() {
        LoggerLoad.info("Clicking on 'Edit your account information' link");
        click(editAccountInfoLink);
    }

    public void enterFirstName(String firstName) {
        LoggerLoad.info("Entering First Name: " + firstName);
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        LoggerLoad.info("Entering Last Name: " + lastName);
        type(lastNameField, lastName);
    }

    public void enterEmail(String email) {
        LoggerLoad.info("Entering Email: " + email);
        type(emailField, email);
    }

    public void enterTelephone(String telephone) {
        LoggerLoad.info("Entering Telephone: " + telephone);
        type(telephoneField, telephone);
    }

    public void clickContinue() {
        LoggerLoad.info("Clicking on 'Continue' to save account details");
        click(continueButton);
    }

    public String getSuccessMessage() {
        LoggerLoad.info("Fetching account update success message");
        customWait.until(ExpectedConditions.visibilityOf(successAlert));
        String actualMsg = successAlert.getText().trim();
        LoggerLoad.info("Success message displayed: " + actualMsg);
        return actualMsg;
    }
}
