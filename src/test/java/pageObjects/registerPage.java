package pageObjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;



public class registerPage  extends basePage{

	public registerPage(WebDriver driver) {
		super(driver);
	}

	
	//locators
	
	@FindBy(css="#input-firstname") WebElement firstnameField;
	@FindBy(css="#input-lastname") WebElement lastnameField;
	@FindBy(css="#input-email") WebElement emailField;
	@FindBy(css="#input-telephone") WebElement telephoneField;
	@FindBy(css="#input-password") WebElement passwordField;
	@FindBy(css="#input-confirm") WebElement cnfpasswordField;
	@FindBy(xpath="//input[@name='agree']") WebElement checkboxField;
    @FindBy(xpath="//input[@value='Continue']") WebElement continueButton;
    @FindBy(xpath="//h1[text()='Your Account Has Been Created!']") WebElement confirmTxt;
    
    //methods 
    
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
        checkboxField.click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void verifyText() {
    	System.out.println(confirmTxt.isDisplayed());
    	String Act_txt=confirmTxt.getText();
    	Assert.assertEquals(Act_txt, "Your Account Has Been Created!");
    }
    
   
    



}
