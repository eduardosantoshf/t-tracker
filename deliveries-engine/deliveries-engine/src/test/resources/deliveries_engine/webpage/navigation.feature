Feature: Website Functionalities Test
    Users should be able to navigate
    between the different pages/ components
    of the website through the navbar

    Background: User accesses website
        Given I access "http://127.0.0.1:8080/"

    Scenario: Navigation between sections
        When I navigate to the Driver Signup section
        Then The driver header should state "Signup as Driver"
        When I navigate to the Store Signup section
        Then The store header should state "Signup as Store"
        When I navigate to the Login page
        Then The login header should state "Login"

    Scenario: Driver Signup
        When I navigate to the Driver Signup section
        Then I fill in the necessary driver information
        And I Press the driver signup button
        Then I should be redirected to the login page
    
    Scenario: Store Signup
        When I navigate to the Store Signup section
        Then I fill in the necessary store information
        And I press the store signup button
        Then I should receive a token

    # Scenario: Navigate to driver signup
    #     When I navigate to the Driver Signup section
    #     Then The header should state "Signup as Driver"
    #     And All the inputs should be present
    
    # Scenario: Navigate to store signup
    #     When I navigate to the Store Signup section
    #     Then The header should state "Signup as Store"
    #     And All the inputs should be present
    
    # Scenario: Navigate to login
    #     When I navigate to the Login page
    #     Then The header should state "Login"
    #     And The inputs for email and password should exist
    #     And The login and register buttons should be present

        
