package pageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;




public class basePage  {

	protected WebDriver driver;
	WebDriverWait ww;
	
	public basePage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver,this);
		ww = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
}
