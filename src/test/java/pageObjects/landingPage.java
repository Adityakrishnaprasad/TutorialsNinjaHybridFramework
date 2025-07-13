package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class landingPage extends basePage{

	public landingPage(WebDriver driver) {
		super(driver);
	}
	
	By userdd=By.xpath("//a[@title='My Account']");
	By reg=By.xpath("//a[normalize-space()='Register']");
	By login=By.xpath("//a[normalize-space()='Login']");
	
	public void clickOnUserdd() {
		WebElement udd = driver.findElement(userdd);
		udd.click();
	}
	
	public void clickOnuserReg() {
		WebElement Reg = driver.findElement(reg);
		Reg.click();
	}
	
	public void clickOnLogin() {
		WebElement Login = driver.findElement(login);
		Login.click();
	}

	

}
