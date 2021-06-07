package deliveries_engine.webpage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.core.Is.is;

// import java.time.LocalDateTime;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NavigationSteps {
    private WebDriver driver = new FirefoxDriver();

    private HomePage homePage;

    @Given("I access {string}")
    public void i_access(String pageUrl) {
        homePage = new HomePage(driver, pageUrl);
    }

    @When("I navigate to the Rider Signup section")
    public void i_navigate_to_the_rider_signup_section() {
        homePage.navigateToRiderSignup();
        driver.close();
    }

    @Then("The header should state {string}")
    public void the_header_should_state(String header) {
        System.out.println(1);
        
    }

    @When("I navigate to the Store Signup section")
    public void i_navigate_to_the_store_signup_section() {
        System.out.println(1);
        
    }

    @When("I navigate to the Login page")
    public void i_navigate_to_the_login_page() {
        System.out.println(1);
        
    }

    
}
