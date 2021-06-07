package deliveries_engine.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private WebDriver driver;

    @FindBy(id = "login-header")
    private WebElement loginHeader;

    @FindBy(id = "loginEmailInput")
    private WebElement loginEmailInput;

    @FindBy(id = "loginPasswordInput")
    private WebElement loginPasswordInput;

    @FindBy(id = "submitLoginBtn")
    private WebElement submitLoginBtn;

    //Constructor
	public LoginPage(WebDriver driver){
		this.driver = driver;
		//Initialise Elements
		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();
	}

    //Constructor
	public LoginPage(WebDriver driver, String page_url){
		this.driver = driver;
		driver.get(page_url);
		//Initialise Elements
		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();
	}

	public String getLoginHeader() {
		return this.loginHeader.getText();
	}
	
	public void fillLoginDetail() {
		loginEmailInput.sendKeys("greenman@mail.org");
		loginPasswordInput.sendKeys("hulksmash1234");
	}

    public void pressLogin() {
        this.submitLoginBtn.click();
    }

}
