package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import testdata.TestDataProvider;

/**
 * Task 1: Automate Login Journey using Parameterization and Validation.
 * Tests multiple sets of credentials (valid, invalid email, invalid password, both invalid)
 * supplied by TestNG DataProvider.
 */
public class LoginTest extends BaseTest {

    @Test(
        dataProvider = "loginData",
        dataProviderClass = TestDataProvider.class,
        description = "Validate Login Journey with parameterized test data sets"
    )
    public void testLoginJourney(String scenario, String username, String password, boolean expectedSuccess) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Executing Scenario: " + scenario);
        System.out.println("Expected Success: " + expectedSuccess);

        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // Verify initial state is the Login page
        Assert.assertTrue(loginPage.isAt(), "Application should be on the Login page before authentication");

        // Perform login action
        loginPage.login(username, password);

        if (expectedSuccess) {
            // Verify navigation to dashboard
            boolean isDashboardVisible = dashboardPage.isDashboardDisplayed();
            Assert.assertTrue(isDashboardVisible, "Valid credentials should successfully navigate to the Dashboard");

            // 1. Verify Dashboard URL
            String currentUrl = dashboardPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("/dashboard"), "Current URL should contain '/dashboard'. Actual: " + currentUrl);

            // 2. Verify Page Title
            String pageTitle = dashboardPage.getPageTitle();
            Assert.assertTrue(pageTitle.contains("Field Force"), "Page title should contain 'Field Force'. Actual: " + pageTitle);

            // 3. Verify visible dashboard element
            Assert.assertTrue(dashboardPage.isUserProfileHeaderVisible(), "User profile header element should be visible on Dashboard");

            // 4. Verify authenticated user details in header
            String loggedInUser = dashboardPage.getLoggedInUserName();
            Assert.assertFalse(loggedInUser.isEmpty(), "Dashboard should display the logged-in user profile name");
            System.out.println("SUCCESS: User successfully authenticated and navigated to Dashboard.");
            System.out.println("Verified URL: " + currentUrl + " | Title: " + pageTitle + " | User: " + loggedInUser);
        } else {
            // Verify that navigation to dashboard did NOT occur
            boolean isDashboardVisible = dashboardPage.isDashboardDisplayed();
            Assert.assertFalse(isDashboardVisible, "Invalid credentials should NOT navigate to the Dashboard");

            // Verify user remains on the Login page or an error message is surfaced
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(
                currentUrl.contains("/auth/login") || currentUrl.endsWith(".com/"),
                "User should remain on the Login URL when authentication fails. Current URL: " + currentUrl
            );

            String errorMsg = loginPage.getErrorMessage();
            if (!errorMsg.isEmpty()) {
                System.out.println("Captured Application Validation/Error Message: '" + errorMsg + "'");
            }
            System.out.println("SUCCESS: Invalid login blocked as expected for scenario: " + scenario);
        }
        System.out.println("--------------------------------------------------");
    }
}
