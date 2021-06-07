package deliveries_engine.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.AfterEach;

// import java.time.LocalDateTime;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NavigationSteps {
    private WebDriver driver = new FirefoxDriver();

    private HomePage homePage;
    private LoginPage loginPage;

    @AfterEach
    void cleanUp() {
        driver.close();
    }

    @Given("I access {string}")
    public void i_access(String pageUrl) {
        homePage = new HomePage(driver, pageUrl);
    }

    @When("I navigate to the Driver Signup section")
    public void i_navigate_to_the_driver_signup_section() {
        homePage.navigateToDriverSignup();
        driver.close();
    }

    @Then("The driver header should state {string}")
    public void the_driver_header_should_state(String header) {
        assertThat( homePage.getDriverHeader(), is(header) );
    }

    @When("I navigate to the Store Signup section")
    public void i_navigate_to_the_store_signup_section() {
        homePage.navigateToStoreSignup();
    }

    @Then("The store header should state {string}")
    public void the_store_header_should_state(String header) {
        assertThat( homePage.getStoreHeader(), is(header) );
    }

    @When("I navigate to the Login page")
    public void i_navigate_to_the_login_page() {
        homePage.navigateToLogin();
    }

    @Then("The login header should state {string}")
    public void the_login_header_should_state(String header) {
        assertThat( loginPage.getLoginHeader(), is(header) );
    }

    @When("I navigate to the Driver Signup section")
    public void i_navigate_to_the_driver_signup_sections() {
        homePage.navigateToDriverSignup();
    }

    @Then("I fill in the necessary information")
    public void i_fill_in_the_necessary_driver_information() {
        homePage.fillDriverSignup();
    }

    @Then("I Press the signup button")
    public void i_press_the_driver_signup_button() {
        homePage.pressSubmitDriver();
    }

    @Then("I should be redirected to the login page")
    public void i_should_be_redirected_to_the_login_page() {
        //TO DO
    }

    @When("I navigate to the Store Signup section")
    public void i_navigate_to_the_store_signup_sections() {
        homePage.navigateToStoreSignup();
    }

    @Then("I fill in the necessary information")
    public void i_fill_in_the_necessary_store_information() {
        homePage.fillStoreSignup();
    }

    @Then("I press the signup button")
    public void i_press_the_store_signup_button() {
        homePage.pressSubmitStore();
    }

    @Then("I should receive a token")
    public void i_should_receive_a_token() {
        // TO DO
    }

}
