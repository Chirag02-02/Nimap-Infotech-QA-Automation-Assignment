package tests;

import base.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PunchInPage;
import utilities.ConfigReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Task 2: Attendance Module – Punch In / Punch Out Functional Verification.
 */
public class PunchInTest extends BaseTest {

    private void captureEvidence(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
            Path dir = Paths.get("screenshots");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path dest = dir.resolve(name + ".png");
            Files.write(dest, bytes);
            System.out.println("[Evidence] Screenshot saved: " + dest.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Could not save screenshot: " + e.getMessage());
        }
    }

    @Test(description = "Verify Punch In and Punch Out functionality in Attendance section")
    public void testPunchInFunctionality() throws Exception {
        System.out.println("\n==================================================");
        System.out.println("EXECUTION: Attendance Module – Punch In / Punch Out Verification");

        // 1. Log in with configured credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // 2. Navigate to Attendance section
        PunchInPage punchInPage = new PunchInPage(driver);
        punchInPage.navigateToAttendance();
        Assert.assertTrue(punchInPage.isAtAttendancePage(), "Must navigate to Attendance section");
        System.out.println("Step 1: Navigated to Attendance page: " + driver.getCurrentUrl());
        captureEvidence("attendance_initial_state");

        // 3. Check Initial Attendance Status
        String initialStatus = punchInPage.getCurrentAttendanceStatus();
        String initialPunchedInCount = punchInPage.getPunchedInCount();
        String initialPunchedOutCount = punchInPage.getPunchedOutCount();
        System.out.println("Step 2: Initial Attendance Status: '" + initialStatus + "'");
        System.out.println("        Punched In Count: " + initialPunchedInCount + " | Punched Out Count: " + initialPunchedOutCount);

        // 4. Verify Punch In
        System.out.println("\nStep 3: Locating and verifying Punch In control...");
        boolean isPunchInPresent = punchInPage.isPunchInControlPresent();
        Assert.assertTrue(isPunchInPresent, "Punch In control must be present in the Attendance section");
        System.out.println("        Punch In control located successfully.");

        System.out.println("        Performing Punch In action...");
        punchInPage.performPunchInAction();

        // Wait up to 5 seconds for application response
        Thread.sleep(3000);
        String toastAfterPunchIn = punchInPage.getToastMessage();
        String statusAfterPunchIn = punchInPage.getCurrentAttendanceStatus();
        String punchedInCountAfter = punchInPage.getPunchedInCount();
        captureEvidence("after_punch_in_action");

        System.out.println("        Response Observation after Punch In:");
        System.out.println("         - Toast Message: " + (toastAfterPunchIn.isEmpty() ? "None observed" : "'" + toastAfterPunchIn + "'"));
        System.out.println("         - Status: '" + statusAfterPunchIn + "'");
        System.out.println("         - Punched In Count: " + punchedInCountAfter);

        // 5. Verify Punch Out
        System.out.println("\nStep 4: Locating and verifying Punch Out control...");
        boolean isPunchOutPresent = punchInPage.isPunchOutControlPresent();
        Assert.assertTrue(isPunchOutPresent, "Punch Out control must be present in the Attendance section");
        System.out.println("        Punch Out control located successfully.");

        System.out.println("        Performing Punch Out action...");
        punchInPage.performPunchOutAction();

        // Wait up to 5 seconds for application response
        Thread.sleep(3000);
        String toastAfterPunchOut = punchInPage.getToastMessage();
        String statusAfterPunchOut = punchInPage.getCurrentAttendanceStatus();
        String punchedOutCountAfter = punchInPage.getPunchedOutCount();
        captureEvidence("after_punch_out_action");

        System.out.println("        Response Observation after Punch Out:");
        System.out.println("         - Toast Message: " + (toastAfterPunchOut.isEmpty() ? "None observed" : "'" + toastAfterPunchOut + "'"));
        System.out.println("         - Status: '" + statusAfterPunchOut + "'");
        System.out.println("         - Punched Out Count: " + punchedOutCountAfter);

        // 6. Final verification table
        System.out.println("\n==================================================");
        System.out.println("SUMMARY OF PUNCH IN / PUNCH OUT VERIFICATION:");
        System.out.println("| Test Scenario    | Expected Result                           | Actual Result                             |");
        System.out.println("| Punch In         | Records employee's starting time          | Action triggered, status: " + statusAfterPunchIn + " |");
        System.out.println("| Punch Out        | Records employee's ending time            | Action triggered, status: " + statusAfterPunchOut + " |");
        System.out.println("| Attendance status| Updates according to punch actions        | Status: '" + statusAfterPunchOut + "', Counts: In=" + punchedInCountAfter + ", Out=" + punchedOutCountAfter + " |");
        System.out.println("==================================================\n");
    }
}
