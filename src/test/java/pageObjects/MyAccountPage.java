package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utilities.LoggerLoad;

public class MyAccountPage extends BasePage {

    public MyAccountPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath="//a[text()='Logout']")
    private WebElement logoutLink;

    @FindBy(xpath="//a[text()='Continue']")
    private WebElement continueButton;

    @FindBy(name="search")
    private WebElement searchField;

    // Direct product link
    @FindBy(xpath="//h4/a[text()='Samsung Galaxy Tab 10.1']")
    private WebElement productLink;

    @FindBy(xpath="//i[@class='fa fa-search']")
    private WebElement searchButton;

    // Main heading of the current page, e.g. 'Account Logout'
    @FindBy(xpath="//div[@id='content']/h1")
    private WebElement pageHeading;

    public void clickLogout() {
        LoggerLoad.info("Clicking on 'Logout' link");
        click(logoutLink);
    }

    public String getPageHeading() {
        customWait.until(ExpectedConditions.visibilityOf(pageHeading));
        String heading = pageHeading.getText().trim();
        LoggerLoad.info("Page heading: " + heading);
        return heading;
    }

    public void clickContinue() {
        LoggerLoad.info("Clicking on 'Continue' button after logout");
        click(continueButton);
    }

    /**
     * @param pName
     */
    public void searchForProduct(String pName) {
        LoggerLoad.info("Searching for product: " + pName);
        type(searchField, pName);
        click(searchButton);
    }

    public void clickOnProduct() {
        LoggerLoad.info("Clicking on product: Samsung Galaxy Tab 10.1");
        click(productLink);
    }

}
