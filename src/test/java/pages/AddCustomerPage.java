package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object representing the Add Customer functionality.
 * Encapsulates navigation, drawer opening, form filling, and submission validation.
 */
public class AddCustomerPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators verified through live application inspection
    private final By myCustomersNav = By.xpath("//span[normalize-space()='My Customers']");
    private final By manageButton = By.xpath("//button[contains(., 'Manage')]");
    private final By newCustomerMenuItem = By.xpath("//li[contains(., 'New Customer')]");
    
    // Form fields inside Add Customer drawer
    private final By leadNameInput = By.name("LeadName");
    private final By personNameInput = By.name("PersonName");
    private final By mobileNoInput = By.name("MobileNo");
    private final By emailInput = By.name("Email");
    private final By addressInput = By.name("Address");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By cancelButton = By.xpath("//button[normalize-space()='Cancel']");
    private final By drawerContainer = By.xpath("//div[contains(@class, 'MuiDrawer-paper') or contains(@class, 'drawer')]");
    private final By toastOrAlert = By.xpath("//*[contains(@class, 'Toastify__toast-body') or contains(@class, 'toast') or @role='alert' or contains(@class, 'MuiAlert-message')]");
    private final By customerSearchInput = By.xpath("//input[contains(@placeholder, 'Search') or @type='search']");

    public AddCustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to the Customers page.
     */
    public void navigateToCustomers() {
        try {
            WebElement nav = wait.until(ExpectedConditions.elementToBeClickable(myCustomersNav));
            nav.click();
            wait.until(ExpectedConditions.urlContains("/customers"));
        } catch (Exception e) {
            driver.get("https://test.fieldforceconnect.com/customers");
            wait.until(ExpectedConditions.urlContains("/customers"));
        }
    }

    /**
     * Opens the Add Customer drawer by clicking Manage -> New Customer.
     */
    public void openAddCustomerDrawer() {
        WebElement manage = wait.until(ExpectedConditions.elementToBeClickable(manageButton));
        manage.click();

        WebElement newCust = wait.until(ExpectedConditions.elementToBeClickable(newCustomerMenuItem));
        newCust.click();

        // Wait for mandatory LeadName input to become visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(leadNameInput));
    }

    /**
     * Checks if the Add Customer form is displayed.
     */
    public boolean isCustomerFormDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(leadNameInput)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enters customer details into the form.
     */
    public void fillCustomerForm(String leadName, String contactPerson, String mobile, String email) {
        if (leadName != null && !leadName.isEmpty()) {
            WebElement nameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(leadNameInput));
            nameEl.clear();
            nameEl.sendKeys(leadName);
            triggerReactEvents(nameEl);
        }

        if (contactPerson != null && !contactPerson.isEmpty()) {
            List<WebElement> personEls = driver.findElements(personNameInput);
            if (!personEls.isEmpty()) {
                personEls.get(0).clear();
                personEls.get(0).sendKeys(contactPerson);
                triggerReactEvents(personEls.get(0));
            }
        }

        if (mobile != null && !mobile.isEmpty()) {
            List<WebElement> mobileEls = driver.findElements(mobileNoInput);
            if (!mobileEls.isEmpty()) {
                mobileEls.get(0).clear();
                mobileEls.get(0).sendKeys(mobile);
                triggerReactEvents(mobileEls.get(0));
            }
        }

        if (email != null && !email.isEmpty()) {
            List<WebElement> emailEls = driver.findElements(emailInput);
            if (!emailEls.isEmpty()) {
                emailEls.get(0).clear();
                emailEls.get(0).sendKeys(email);
                triggerReactEvents(emailEls.get(0));
            }
        }
    }

    /**
     * Clicks the Save button.
     */
    public void clickSave() {
        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        try {
            save.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", save);
        }
    }

    /**
     * Clicks the Cancel button.
     */
    public void clickCancel() {
        WebElement cancel = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        try {
            cancel.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancel);
        }
    }

    /**
     * Captures any visible toast or alert notification after submission.
     */
    public String getToastMessage() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
            WebElement alert = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastOrAlert));
            return alert.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Verifies if the customer drawer has closed after successful submission.
     */
    public boolean isDrawerClosed() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
            return shortWait.until(ExpectedConditions.invisibilityOfElementLocated(leadNameInput));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the created customer appears in the customer list or table.
     */
    public boolean isCustomerInTable(String customerName) {
        try {
            By customerLocator = By.xpath("//*[contains(text(), '" + customerName + "')]");
            return !driver.findElements(customerLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void triggerReactEvents(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element
            );
        } catch (Exception ignored) {}
    }
}

