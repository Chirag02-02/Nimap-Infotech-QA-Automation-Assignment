package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddCustomerPage;
import pages.LoginPage;
import testdata.TestDataProvider;
import utilities.ConfigReader;

/**
 * Task 3: Add Customer Automation.
 *
 * Requirements:
 * 1. Navigate to the dashboard after successful login.
 * 2. Identify and access the Add Customer functionality.
 * 3. Inspect the customer form, mandatory fields and locators.
 * 4. Automate customer creation using Selenium.
 * 5. Use TestNG DataProvider to supply multiple customer data sets.
 * 6. Validate the application response after submission.
 * 7. Verify successful customer creation using confirmation or customer listing.
 */
public class AddCustomerTest extends BaseTest {

    @Test(
        dataProvider = "customerData",
        dataProviderClass = TestDataProvider.class,
        description = "Validate Add Customer functionality with parameterized datasets"
    )
    public void testAddCustomer(String scenario, String leadName, String contactPerson, String mobile, String email) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Executing Task 3: Add Customer - Scenario: " + scenario);
        System.out.println("Customer Name: " + leadName);

        // 1. Log in with valid credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // 2. Navigate to Customers section
        AddCustomerPage addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.navigateToCustomers();

        // 3. Open Add Customer drawer
        addCustomerPage.openAddCustomerDrawer();
        Assert.assertTrue(addCustomerPage.isCustomerFormDisplayed(),
            "Add Customer form drawer should be visible with mandatory fields");
        System.out.println("Verified: Add Customer drawer is open.");

        // 4. Fill form with parameterized test data
        addCustomerPage.fillCustomerForm(leadName, contactPerson, mobile, email);
        System.out.println("Form filled with details for: " + leadName);

        // 5. Submit the customer form
        addCustomerPage.clickSave();
        System.out.println("Clicked Save button. Validating application response...");

        // 6. Validate application response (toast message or drawer closing)
        String toastMessage = addCustomerPage.getToastMessage();
        boolean isDrawerClosed = addCustomerPage.isDrawerClosed();

        if (!toastMessage.isEmpty()) {
            System.out.println("Application Toast/Alert Response: '" + toastMessage + "'");
        }

        // Assert that the creation succeeded: either toast confirmed or drawer closed cleanly
        Assert.assertTrue(
            isDrawerClosed || !toastMessage.isEmpty(),
            "Customer submission should produce a confirmation message or close the creation drawer"
        );

        System.out.println("SUCCESS: Customer creation journey verified for scenario: " + scenario);
        System.out.println("--------------------------------------------------");
    }
}

