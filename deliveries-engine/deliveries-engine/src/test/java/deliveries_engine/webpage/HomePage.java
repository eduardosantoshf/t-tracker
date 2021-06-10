package deliveries_engine.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;

	@FindBy(xpath = "/html/body/div/div")
	private WebElement loader;

    @FindBy(id = "main-header")
    private WebElement mainHeader;

	@FindBy(id = "driver-header")
    private WebElement driverHeader;

	@FindBy(id = "linkDrivers")
    private WebElement navLinkDrivers;

	@FindBy(id = "linkStores")
    private WebElement navLinkStores;

	@FindBy(id = "linkLogin")
    private WebElement navLinkLogin;

	@FindBy(id = "driverNameInput")
    private WebElement driverNameInput;

	@FindBy(id = "driverEmailInput")
    private WebElement driverEmailInput;

	@FindBy(id = "driverUsernameInput")
    private WebElement driverUsernameInput;

	@FindBy(id = "driverPasswordInput")
    private WebElement driverPasswordInput;

	@FindBy(id = "driverPhoneNumberInput")
    private WebElement driverPhoneNumberInput;

	@FindBy(id = "driverAddressInput")
    private WebElement driverAddressInput;

	@FindBy(id = "driverZipCodeInput")
    private WebElement driverZipCodeInput;

	@FindBy(id = "driverCityInput")
    private WebElement driverCityInput;

	@FindBy(id = "driverSignupSubmitBtn")
	private WebElement driverSignupSubmitBtn;

	@FindBy(id = "store-header")
    private WebElement storeHeader;

	@FindBy(id = "storeSignupName")
	private WebElement storeSignupName;

	@FindBy(id = "storeSignupOwnerName")
	private WebElement storeSignupOwnerName;

	@FindBy(id = "storeSignupSubmitBtn")
	private WebElement storeSignupSubmitBtn;

    //Constructor
	public HomePage(WebDriver driver){
		this.driver = driver;
		//Initialise Elements
		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();
	}

    //Constructor
	public HomePage(WebDriver driver, String page_url){
		this.driver = driver;
		driver.get(page_url);
		//Initialise Elements
		PageFactory.initElements(driver, this);
		driver.manage().window().maximize();

		WebDriverWait wait = new WebDriverWait(driver, 5000L);
		//wait.until(ExpectedConditions.visibilityOf(loader)); // wait for loader to appear
		wait.until(ExpectedConditions.invisibilityOf(loader)); // wait for loader to disappear
	}

	public String getMainHeader() {
		return this.mainHeader.getText();
	}

	public void navigateToDriverSignup() {
		navLinkDrivers.click();
	}

	public void navigateToStoreSignup() {
		navLinkStores.click();
	}

	public void navigateToLogin() {
		navLinkLogin.click();
	}

	public String getDriverHeader() {
		return this.driverHeader.getText();
	}

	public void fillDriverSignup() {
		driverNameInput.sendKeys("Bruce Banner");
		driverEmailInput.sendKeys("greenman@mail.org");
		driverUsernameInput.sendKeys("IncredibleHulk");
		driverPasswordInput.sendKeys("hulksmash1234");
		driverPhoneNumberInput.sendKeys("933392000");
		driverAddressInput.sendKeys("Stark Tower");
		driverZipCodeInput.sendKeys("9999-000");
		driverCityInput.sendKeys("NYC");
	}

	public void pressSubmitDriver() {
		this.driverSignupSubmitBtn.click();
	}

	public String getStoreHeader() {
		return this.storeHeader.getText();
	}
	
	public void fillStoreSignup() {
		storeSignupName.sendKeys("The New Amazon");
		storeSignupOwnerName.sendKeys("Gill Bates");
	}

	public void pressSubmitStore() {
		this.storeSignupSubmitBtn.click();
	}

}
