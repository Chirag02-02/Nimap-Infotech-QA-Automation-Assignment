package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object representing the Login Page of Field Force Connect.
 * Implements Page Object Model (POM) to separate page locators and actions from tests.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators verified through live application inspection
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By signInButton = By.xpath("//button[@type='submit' or contains(., 'Sign In')]");
    private final By toastOrAlert = By.xpath("//*[contains(@class, 'Toastify__toast-body') or contains(@class, 'toast') or @role='alert' or contains(@class, 'MuiAlert-message')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Checks if currently on the Login page.
     */
    public boolean isAt() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
            return driver.getCurrentUrl().contains("/auth/login") || driver.getTitle().contains("Field Force");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enters username into the username input field.
     * Fires input and change events for React synthetic event compatibility.
     */
    public void enterUsername(String username) {
        WebElement userElem = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        userElem.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
        if (username != null && !username.isEmpty()) {
            userElem.sendKeys(username);
        }
        triggerReactInputEvents(userElem);
    }

    /**
     * Enters password into the password input field.
     * Fires input and change events for React synthetic event compatibility.
     */
    public void enterPassword(String password) {
        WebElement passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passElem.sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"), org.openqa.selenium.Keys.BACK_SPACE);
        if (password != null && !password.isEmpty()) {
            passElem.sendKeys(password);
        }
        triggerReactInputEvents(passElem);
    }

    /**
     * Checks whether the Sign In button is enabled.
     */
    public boolean isSignInButtonEnabled() {
        try {
            WebElement btn = driver.findElement(signInButton);
            String disabledAttr = btn.getAttribute("disabled");
            String classAttr = btn.getAttribute("class");
            return disabledAttr == null && (classAttr == null || !classAttr.contains("Mui-disabled"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks the Sign In button.
     */
    public void clickSignIn() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(signInButton));
        try {
            btn.click();
        } catch (Exception e) {
            // Fallback for click interception on overlay
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    /**
     * Performs complete login action with given credentials.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        try {
            Thread.sleep(500); // brief settling time for React state update
        } catch (InterruptedException ignored) {}
        clickSignIn();
    }

    /**
     * Captures any visible error message / toast if displayed.
     */
    public String getErrorMessage() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement alert = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastOrAlert));
            return alert.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private void triggerReactInputEvents(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element
            );
        } catch (Exception ignored) {}
    }
}

