package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object representing the Attendance module with Punch In & Punch Out controls.
 */
public class PunchInPage {

    private final WebDriver driver;
    private final WebDriverWait wait5s;

    // Locators inside Attendance section
    private final By attendanceNav = By.xpath("//span[normalize-space()='Attendance']");
    private final By memberCard = By.xpath("//div[contains(@class, '_memberCard_')]");
    private final By memberStatus = By.xpath("//div[contains(@class, '_status_')]//span");
    private final By punchedInMetricCard = By.xpath("//div[contains(@class, '_summaryItem_') and contains(., 'Punched In')]");
    private final By punchedOutMetricCard = By.xpath("//div[contains(@class, '_summaryItem_') and contains(., 'Punched Out')]");
    private final By directPunchInBtn = By.xpath("//button[contains(translate(text(), 'PUNCH IN', 'punch in'), 'punch in')] | //div[contains(@class, '_summaryItem_') and contains(., 'Punched In')]");
    private final By directPunchOutBtn = By.xpath("//button[contains(translate(text(), 'PUNCH OUT', 'punch out'), 'punch out')] | //div[contains(@class, '_summaryItem_') and contains(., 'Punched Out')]");
    private final By toastOrAlert = By.xpath("//*[contains(@class, 'Toastify__toast-body') or contains(@class, 'toast') or @role='alert' or contains(@class, 'MuiAlert-message')]");
    private final By attendanceTable = By.xpath("//table");

    public PunchInPage(WebDriver driver) {
        this.driver = driver;
        this.wait5s = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void navigateToAttendance() {
        try {
            WebElement nav = wait5s.until(ExpectedConditions.elementToBeClickable(attendanceNav));
            nav.click();
            wait5s.until(ExpectedConditions.urlContains("/attendance"));
        } catch (Exception e) {
            driver.get("https://test.fieldforceconnect.com/attendance");
            wait5s.until(ExpectedConditions.urlContains("/attendance"));
        }
    }

    public boolean isAtAttendancePage() {
        return driver.getCurrentUrl().contains("/attendance");
    }

    public String getCurrentAttendanceStatus() {
        try {
            WebElement statusEl = wait5s.until(ExpectedConditions.visibilityOfElementLocated(memberStatus));
            return statusEl.getText().trim();
        } catch (Exception e) {
            return "Status unavailable";
        }
    }

    public boolean isPunchInControlPresent() {
        return !driver.findElements(punchedInMetricCard).isEmpty() || !driver.findElements(directPunchInBtn).isEmpty();
    }

    public boolean isPunchOutControlPresent() {
        return !driver.findElements(punchedOutMetricCard).isEmpty() || !driver.findElements(directPunchOutBtn).isEmpty();
    }

    public void performPunchInAction() {
        List<WebElement> buttons = driver.findElements(directPunchInBtn);
        if (!buttons.isEmpty()) {
            WebElement target = buttons.get(0);
            try {
                target.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
            }
        }
    }

    public void performPunchOutAction() {
        List<WebElement> buttons = driver.findElements(directPunchOutBtn);
        if (!buttons.isEmpty()) {
            WebElement target = buttons.get(0);
            try {
                target.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
            }
        }
    }

    public String getPunchedInCount() {
        try {
            WebElement el = driver.findElement(punchedInMetricCard);
            return el.findElement(By.tagName("strong")).getText().trim();
        } catch (Exception e) {
            return "0";
        }
    }

    public String getPunchedOutCount() {
        try {
            WebElement el = driver.findElement(punchedOutMetricCard);
            return el.findElement(By.tagName("strong")).getText().trim();
        } catch (Exception e) {
            return "0";
        }
    }

    public String getToastMessage() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement alert = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastOrAlert));
            return alert.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isAttendanceTableDisplayed() {
        try {
            return wait5s.until(ExpectedConditions.visibilityOfElementLocated(attendanceTable)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
