package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object representing the Punch In / Attendance functionality.
 * Inspects actual application behaviour for Punch In controls on the web portal.
 */
public class PunchInPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators verified through live application inspection
    private final By attendanceNav = By.xpath("//span[normalize-space()='Attendance']");
    private final By punchStatusCard = By.xpath("//*[contains(text(), 'Punched In')]");
    private final By directPunchInButton = By.xpath("//button[contains(translate(text(), 'PUNCH IN', 'punch in'), 'punch in')]");
    private final By attendanceTable = By.xpath("//table");
    private final By toastOrAlert = By.xpath("//*[contains(@class, 'Toastify__toast-body') or contains(@class, 'toast') or @role='alert' or contains(@class, 'MuiAlert-message')]");

    public PunchInPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the Attendance page.
     */
    public void navigateToAttendance() {
        try {
            WebElement nav = wait.until(ExpectedConditions.elementToBeClickable(attendanceNav));
            nav.click();
            wait.until(ExpectedConditions.urlContains("/attendance"));
        } catch (Exception e) {
            driver.get("https://test.fieldforceconnect.com/attendance");
            wait.until(ExpectedConditions.urlContains("/attendance"));
        }
    }

    /**
     * Checks if the Attendance page URL is loaded.
     */
    public boolean isAtAttendancePage() {
        return driver.getCurrentUrl().contains("/attendance");
    }

    /**
     * Checks whether an interactive Punch In button exists in the DOM.
     */
    public boolean isDirectPunchInButtonPresent() {
        List<WebElement> buttons = driver.findElements(directPunchInButton);
        return !buttons.isEmpty() && buttons.get(0).isDisplayed();
    }

    /**
     * Attempts to click the Punch In button if available.
     */
    public void clickPunchInIfAvailable() {
        if (isDirectPunchInButtonPresent()) {
            driver.findElement(directPunchInButton).click();
        }
    }

    /**
     * Checks if the Attendance log table is displayed.
     */
    public boolean isAttendanceTableDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(attendanceTable)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Captures any toast or popup message that appears.
     */
    public String getToastMessage() {
        try {
            WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement alert = toastWait.until(ExpectedConditions.visibilityOfElementLocated(toastOrAlert));
            return alert.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if the Punch In status card is visible on the dashboard.
     */
    public boolean isPunchStatusCardVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(punchStatusCard)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

