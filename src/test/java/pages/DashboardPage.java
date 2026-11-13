package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object representing the Post-Login Dashboard of Field Force Connect.
 */
public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators verified through live application inspection
    private final By userProfileButton = By.xpath("//button[contains(@class, '_userName_')]");
    private final By pageNameHeading = By.xpath("//p[contains(@class, '_pageName_')]");
    private final By myCustomersNav = By.xpath("//span[normalize-space()='My Customers']");
    private final By attendanceNav = By.xpath("//span[normalize-space()='Attendance']");
    private final By punchedInCard = By.xpath("//*[contains(text(), 'Punched In')]");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Verifies if the dashboard is currently displayed after successful authentication.
     */
    public boolean isDashboardDisplayed() {
        try {
            wait.until(ExpectedConditions.urlContains("/dashboard"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(userProfileButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current page URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks if the user profile header button is visible.
     */
    public boolean isUserProfileHeaderVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(userProfileButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the displayed authenticated user's name from the top header bar.
     */
    public String getLoggedInUserName() {
        try {
            WebElement userElem = wait.until(ExpectedConditions.visibilityOfElementLocated(userProfileButton));
            return userElem.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Navigates to the Customers section.
     */
    public void navigateToCustomers() {
        try {
            WebElement custElem = wait.until(ExpectedConditions.elementToBeClickable(myCustomersNav));
            custElem.click();
        } catch (Exception e) {
            driver.get("https://test.fieldforceconnect.com/customers");
        }
    }

    /**
     * Navigates to the Attendance section.
     */
    public void navigateToAttendance() {
        try {
            WebElement attElem = wait.until(ExpectedConditions.elementToBeClickable(attendanceNav));
            attElem.click();
        } catch (Exception e) {
            driver.get("https://test.fieldforceconnect.com/attendance");
        }
    }

    /**
     * Verifies if the punched in statistic card is present on the dashboard.
     */
    public boolean isPunchCardPresent() {
        try {
            return !driver.findElements(punchedInCard).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
