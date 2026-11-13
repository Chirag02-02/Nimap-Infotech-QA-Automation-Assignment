package testdata;

import org.testng.annotations.DataProvider;
import utilities.ConfigReader;

/**
 * Supplies parameterized test data for TestNG tests.
 * Never hardcodes passwords; retrieves valid credentials dynamically from ConfigReader.
 */
public class TestDataProvider {

    /**
     * Parameterized data for Task 1: Login Journey.
     * Contains 4 scenarios required by the assignment:
     * 1. Valid credentials
     * 2. Invalid email/mobile number
     * 3. Invalid password
     * 4. Both invalid credentials
     *
     * Format: { Scenario Description, Username, Password, Expected Success }
     */
    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        String validUsername = ConfigReader.getValidUsername();
        String validPassword = ConfigReader.getValidPassword();

        return new Object[][] {
            { "Valid Credentials Scenario", validUsername, validPassword, true },
            { "Invalid Email/Mobile Scenario", "invalid_user_automation@testmail.com", validPassword, false },
            { "Invalid Password Scenario", validUsername, "WrongPassword@999!", false },
            { "Both Invalid Credentials Scenario", "nonexistent_account_qa@testmail.com", "InvalidPass@123#", false }
        };
    }

    /**
     * Parameterized data for Task 3: Add Customer.
     * Supplies multiple realistic datasets with uniquely generated customer names to prevent duplicates.
     *
     * Format: { Scenario Description, Lead/Customer Name, Contact Person, Mobile, Email }
     */
    @DataProvider(name = "customerData")
    public static Object[][] getCustomerData() {
        long timestamp = System.currentTimeMillis() % 100000;
        return new Object[][] {
            {
                "Complete Customer Profile",
                "Nimap Corp " + timestamp,
                "Rajesh Sharma",
                "98200" + String.format("%05d", timestamp),
                "rajesh." + timestamp + "@nimapinfotech.com"
            },
            {
                "Mandatory Fields Only",
                "Apex Sol " + (timestamp + 1),
                "",
                "",
                ""
            }
        };
    }
}
