package pageObjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;



public class registerPage  extends basePage{

	public registerPage(WebDriver driver) {
		super(driver);
	}

	
	//locators
	
	@FindBy(css="#input-firstname") private WebElement firstnameField;
    @FindBy(css="#input-lastname") private WebElement lastnameField;
    @FindBy(css="#input-email") private WebElement emailField;
    @FindBy(css="#input-telephone") private WebElement telephoneField;
    @FindBy(css="#input-password") private WebElement passwordField;
    @FindBy(css="#input-confirm") private WebElement cnfpasswordField;
    @FindBy(xpath="//input[@name='agree']") private WebElement checkboxField;
    @FindBy(xpath="//input[@value='Continue']") private WebElement continueButton;
    @FindBy(xpath="//h1[text()='Your Account Has Been Created!']") private WebElement confirmTxt;

    public void enterFirstName(String firstName) {
        firstnameField.clear();
        firstnameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastnameField.clear();
        lastnameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterTelephone(String telephone) {
        telephoneField.clear();
        telephoneField.sendKeys(telephone);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        cnfpasswordField.clear();
        cnfpasswordField.sendKeys(confirmPassword);
    }

    public void clickCheckbox() {
    	ww.until(ExpectedConditions.elementToBeClickable(checkboxField)).click();
    }

    public void clickContinueButton() {
        
        ww.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }

    public void verifyText() {
        ww.until(ExpectedConditions.visibilityOf(confirmTxt));
        String actTxt=confirmTxt.getText();
        Assert.assertEquals(actTxt, "Your Account Has Been Created!");
    }

   
    
   
    



}
