package base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utilities.ConfigReader;
import utilities.DriverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * BaseTest serves as the parent class for all TestNG test classes.
 * Handles driver initialization, teardown, and failure screenshot capture.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        try {
            driver.get(ConfigReader.getAppUrl());
        } catch (org.openqa.selenium.TimeoutException e) {
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.stop();");
            } catch (Exception ignored) {}
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot(result.getName());
        }
        DriverFactory.quitDriver(driver);
    }

    public String captureScreenshot(String testName) {
        if (driver == null) {
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] sourceBytes = ts.getScreenshotAs(OutputType.BYTES);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path dir = Paths.get("screenshots");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path destination = dir.resolve(testName + "_" + timestamp + ".png");
            Files.write(destination, sourceBytes);
            System.out.println("Screenshot captured for failed test: " + destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}

