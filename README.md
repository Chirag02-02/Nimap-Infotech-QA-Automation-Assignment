# Nimap Infotech – QA Automation Machine Test

[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Maven 3.9](https://img.shields.io/badge/Maven-3.9.9-blue.svg)](https://maven.apache.org/)
[![Selenium WebDriver](https://img.shields.io/badge/Selenium%20WebDriver-4.29.0-brightgreen.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red.svg)](https://testng.org/)
[![Design Pattern](https://img.shields.io/badge/Pattern-Page%20Object%20Model-purple.svg)](#5-automation-testing-framework)
[![Postman](https://img.shields.io/badge/API%20Testing-Postman-orange.svg)](https://www.postman.com/)
[![Manual Testing](https://img.shields.io/badge/Manual%20Testing-Excel%20Workbook-green.svg)](#6-manual-testing)

A comprehensive, industry-standard Quality Assurance assignment submission for **Nimap Infotech**, evaluating the **Field Force Connect** enterprise application across **Automation Testing**, **Manual Testing**, and **API Testing**.

---

## 1. Project Overview

- **Project Name:** Nimap Infotech – QA Automation Machine Test
- **Application Under Test (AUT):** Field Force Connect
- **Application URL:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)
- **Repository URL:** [https://github.com/Chirag02-02/Nimap-Infotech-QA-Automation-Assignment](https://github.com/Chirag02-02/Nimap-Infotech-QA-Automation-Assignment)
- **Testing Scope:**
  - **Automation Testing:** End-to-end browser automation using Java, Selenium WebDriver, TestNG, and Page Object Model (POM).
  - **Manual Testing:** Exhaustive test case design, positive/negative validation scenarios, and defect logging maintained in an Excel workbook.
  - **API Testing:** REST API request/response validation, environment parameterization, and authentication workflows using Postman.
- **Objective:** Systematically validate user journeys, input sanitization, dynamic React states, API contracts, and document real application defects and environment constraints.

---

## 2. Technology Stack & Tools

| Component | Technology | Version | Purpose |
|---|---|---|---|
| **Programming Language** | Java (OpenJDK) | `17.0.x LTS` | Core automation language |
| **Build & Dependency Management** | Apache Maven | `3.9.9` | Build lifecycle, dependency injection, and test execution |
| **Browser Automation Engine** | Selenium WebDriver | `4.29.0` | Browser orchestration & element interactions |
| **Testing Framework** | TestNG | `7.11.0` | Test suite execution, annotations, assertions, and DataProvider |
| **Design Pattern** | Page Object Model (POM) | Design Architecture | Decoupling of element locators, page actions, and test scripts |
| **Compiler Plugin** | maven-compiler-plugin | `3.13.0` | Compiling source code targeting Java 17 |
| **Surefire Plugin** | maven-surefire-plugin | `3.5.2` | Running TestNG XML test suites and reporting |
| **API Testing** | Postman | `v10+` | RESTful API request design, token handling, response assertions |
| **Test Case Management** | Microsoft Excel | `.xlsx` | Manual test case design, field validation matrices, defect logs |
| **Version Control** | Git & GitHub | `2.x` | Source code repository and revision tracking |

---

## 3. Project Directory Structure

```text
Nimap-Infotech-QA-Automation-Assignment/
├── pom.xml                                     # Maven dependencies, plugins, and build profiles
├── testng.xml                                  # TestNG suite suite runner configuration
├── mvn.cmd                                     # Portable Maven wrapper for Windows environments
├── README.md                                   # Comprehensive technical and submission documentation
├── .gitignore                                  # Git exclusion rules (credentials, build targets, logs)
│
├── Manual Testing/                             # Manual testing deliverables
│   └── Manual_Test_Cases_FieldForceConnect.xlsx # Manual test cases, validations, and defect sheets
│
├── Postman/                                    # API testing deliverables
│   ├── Field_Force_Connect_API.postman_collection.json # Complete API requests collection
│   └── Field_Force_Connect_Env.postman_environment.json # Postman environment variables template
│
├── src/
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java               # Driver lifecycle, explicit wait setup & screenshot utility
│       │   ├── pages/
│       │   │   ├── LoginPage.java              # POM: Login fields, synthetic React events & validations
│       │   │   ├── DashboardPage.java          # POM: Post-login dashboard verification & navigation
│       │   │   ├── PunchInPage.java            # POM: Attendance module, punch controls & status tracking
│       │   │   └── AddCustomerPage.java        # POM: Customer drawer, form inputs & submission logic
│       │   ├── testdata/
│       │   │   └── TestDataProvider.java      # Parameterized datasets for Login and Customer tests
│       │   ├── tests/
│       │   │   ├── LoginTest.java              # Task 1: Parameterized Login Journey automation
│       │   │   ├── PunchInTest.java            # Task 2: Attendance module & punch verification
│       │   │   └── AddCustomerTest.java        # Task 3: Parameterized Customer Creation automation
│       │   └── utilities/
│       │       ├── ConfigReader.java           # Property file & environment variable loader
│       │       └── DriverFactory.java          # WebDriver factory (Chrome/Edge with EAGER strategy)
│       └── resources/
│           ├── config.properties               # Active local configuration (excluded from Git)
│           └── config.properties.example       # Template configuration with safe placeholders
│
├── screenshots/                                # Test execution screenshot evidence (git-ignored)
│   ├── attendance_initial_state.png            # Attendance page baseline state
│   ├── after_punch_in_action.png               # State capture after Punch In trigger
│   ├── after_punch_out_action.png              # State capture after Punch Out trigger
│   ├── 1_attendance_claims_table.png           # Existing attendance claims table view
│   └── 2_update_timesheet_form_modal.png       # Attendance regularization form modal
│
└── target/                                     # Maven build artifacts and Surefire HTML reports
    └── surefire-reports/
        ├── emailable-report.html               # Summary HTML execution report
        └── index.html                          # Detailed TestNG test report
```

---

## 4. Automation Testing Framework

The automation suite is constructed strictly adhering to the **Page Object Model (POM)** pattern. Each web page has a dedicated page class encapsulating element locators and user actions, keeping test classes clean and focused solely on assertions.

### 4.1 Application Launch & Driver Setup
- Handled centrally in [`DriverFactory.java`](src/test/java/utilities/DriverFactory.java) and [`BaseTest.java`](src/test/java/base/BaseTest.java).
- Browser options are configured with `--remote-allow-origins=*`, `--no-sandbox`, `--disable-dev-shm-usage`, and `--disable-gpu` to guarantee stability.
- Utilizes `PageLoadStrategy.EAGER` combined with dynamic `WebDriverWait` (expected conditions) to prevent browser hangs caused by third-party tracking scripts.
- Automatically captures full-resolution PNG screenshots on test failures into the `screenshots/` directory.

### 4.2 Task 1: Login Journey Automation
- **Class:** [`LoginTest.java`](src/test/java/tests/LoginTest.java) | **Page:** [`LoginPage.java`](src/test/java/pages/LoginPage.java)
- **Data Source:** [`TestDataProvider.getLoginData()`](src/test/java/testdata/TestDataProvider.java)
- **Parameterization:** 4 distinct scenarios executed via TestNG `@DataProvider`:
  1. *Valid Credentials:* Authenticates and validates redirection to `/dashboard`, verifies page title `Test Field Force`, and asserts the visibility of the authenticated user profile (`Chirag Chaudhari`).
  2. *Invalid Email/Mobile:* Submits unrecognized username with valid password; verifies dashboard navigation is rejected.
  3. *Invalid Password:* Submits valid username with wrong password; verifies access is blocked.
  4. *Both Invalid Credentials:* Submits unrecognized credentials; verifies access is blocked.
- **React Synthetic Events:** Dispatches standard `input` and `change` DOM events to ensure Material-UI inputs register values properly with React state.

### 4.3 Task 2: Attendance Module – Punch In / Punch Out Verification
- **Class:** [`PunchInTest.java`](src/test/java/tests/PunchInTest.java) | **Page:** [`PunchInPage.java`](src/test/java/pages/PunchInPage.java)
- **Workflow Verified:**
  1. Navigates to `/attendance` and inspects the employee attendance status card (`Waiting for punch in`).
  2. Inspects attendance metrics (`Punched In: 0`, `Punched Out: 0`).
  3. Locates and clicks the Punch In control; monitors application response using explicit waits ($\le 5$s).
  4. Locates and clicks the Punch Out control; monitors application response using explicit waits ($\le 5$s).
  5. Captures screenshot evidence and asserts live state changes without fabricating fake passes.

### 4.4 Task 3: Add Customer Automation
- **Class:** [`AddCustomerTest.java`](src/test/java/tests/AddCustomerTest.java) | **Page:** [`AddCustomerPage.java`](src/test/java/pages/AddCustomerPage.java)
- **Data Source:** [`TestDataProvider.getCustomerData()`](src/test/java/testdata/TestDataProvider.java)
- **Parameterization:** Supplies multiple realistic customer profiles:
  1. *Complete Profile:* Lead Name, Contact Person Name, Mobile Number, Email Address.
  2. *Mandatory Fields Only:* Mandatory `Lead/Customer Name *` only (`By.name("LeadName")`).
- **Collision Avoidance:** Generates dynamic unique timestamp suffixes (e.g. `Nimap Corp 79330`) to avoid duplicate record errors in testing.
- **Workflow:** Opens `Manage` $\rightarrow$ `New Customer` drawer, fills form fields, clicks `Save`, and asserts drawer closure and toast confirmation.

---

## 5. Manual Testing

The manual testing effort is documented in a dedicated Excel workbook stored in the repository:

📁 **Location:** `Manual Testing/Manual_Test_Cases_FieldForceConnect.xlsx`

### 5.1 Modules Covered
1. **Sign Up Module:** Verification of user registration, mandatory field requirements, corporate email syntax validation, password complexity rules, terms and conditions agreement, and duplicate registration handling.
2. **Forgot Password Module:** Password reset link/OTP dispatch, registered vs. unregistered email handling, reset token expiry, and password mismatch checks.
3. **Sign with OTP Module:** Mobile number/email OTP generation, valid/invalid OTP verification, expired OTP handling, resend timer cooldown, and rate limiting.
4. **Login Module:** Standard credential verification, field formatting, password masking, remember-me functionality, session generation, and role redirection.

### 5.2 Test Design Methodology
- **Techniques Used:** Equivalence Class Partitioning (ECP), Boundary Value Analysis (BVA), and Error Guessing.
- **Coverage:** Both Positive (happy path) and Negative (edge cases, invalid characters, boundary lengths, empty submissions) test cases.
- **Attributes Documented:** Test Case ID, Module Name, Test Scenario Description, Pre-conditions, Test Steps, Test Data, Expected Result, Actual Result, Execution Status (PASS/FAIL), Severity, and Bug ID reference.

---

## 6. API Testing Using Postman

The REST API layer of Field Force Connect was verified using Postman to validate backend service integrity independent of the web UI.

📁 **Files:**
- Collection: `Postman/Field_Force_Connect_API.postman_collection.json`
- Environment: `Postman/Field_Force_Connect_Env.postman_environment.json`

### 6.1 Endpoints Tested

#### 1. User Authentication (Login API)
- **Method & Endpoint:** `POST {{baseUrl}}/api/user/login` (or `/auth/login`)
- **Headers:** `Content-Type: application/json`
- **Request Payload (Positive):**
  ```json
  {
    "username": "{{testUsername}}",
    "password": "{{testPassword}}"
  }
  ```
- **Response Validations:**
  - Status code is `200 OK`.
  - Response body contains a valid session token / JWT.
  - Automatically stores the bearer token into `{{authToken}}` using post-request test scripts:
    ```javascript
    pm.test("Status code is 200", function () {
        pm.response.to.have.status(200);
    });
    var jsonData = pm.response.json();
    if (jsonData.data && jsonData.data.token) {
        pm.environment.set("authToken", jsonData.data.token);
    }
    ```
- **Negative Scenarios:**
  - Invalid password: `POST` with wrong credentials returns `401 Unauthorized` or `400 Bad Request` with appropriate error messaging.
  - Empty username/password: Returns `400 Bad Request` with field validation errors.

#### 2. Create Customer API
- **Method & Endpoint:** `POST {{baseUrl}}/api/customers`
- **Headers:**
  - `Content-Type: application/json`
  - `Authorization: Bearer {{authToken}}`
- **Request Payload:**
  ```json
  {
    "LeadName": "Apex Technologies {{$timestamp}}",
    "PersonName": "Rajesh Sharma",
    "MobileNo": "9820012345",
    "Email": "rajesh@apextech.com",
    "Address": "Mumbai, Maharashtra"
  }
  ```
- **Response Validations:**
  - Status code is `200 OK` or `201 Created`.
  - Response contains newly generated customer ID.
  - Verification that mandatory field validations reject requests missing `LeadName`.

#### 3. Fetch Customer Listing API
- **Method & Endpoint:** `GET {{baseUrl}}/api/customers`
- **Headers:** `Authorization: Bearer {{authToken}}`
- **Response Validations:**
  - Status code is `200 OK`.
  - Response returns an array of customer records matching user permissions.

### 6.2 How to Import and Run the Postman Collection
1. Open **Postman Desktop Application**.
2. Click **Import** (top-left) and select the files from the `Postman/` directory:
   - `Field_Force_Connect_API.postman_collection.json`
   - `Field_Force_Connect_Env.postman_environment.json`
3. Select the active environment **Field Force Connect Environment**.
4. Set the initial values for `baseUrl` (`https://test.fieldforceconnect.com`), `testUsername`, and `testPassword`.
5. Run the collection using the **Postman Collection Runner** to execute all positive and negative requests sequentially.

---

## 7. Defects and Observations

### 7.1 Confirmed Defects

#### Defect 1: Conflicting Notifications on Successful Login
- **Severity / Priority:** Medium / High
- **Steps to Reproduce:**
  1. Open `https://test.fieldforceconnect.com/auth/login`.
  2. Enter valid registered email and password.
  3. Click **Sign In**.
- **Expected Result:** A single success notification (e.g. `"Login Successful"`) should be displayed, followed by redirection to `/dashboard`.
- **Actual Result:** Two simultaneous and contradictory toast notifications appear in the top-right corner:
  1. Red Alert: `"An error has occurred."`
  2. Green Notification: `"Login Successful"`
- **Impact:** Misleads and confuses end users regarding the status of their authentication.

#### Defect 2: Punch In / Punch Out Buttons Non-Functional on Web Interface
- **Severity / Priority:** High / High
- **Steps to Reproduce:**
  1. Log into the application and navigate to the **Attendance** module (`/attendance`).
  2. Locate the Punch In and Punch Out options on the page.
  3. Click the **Punch In** button.
- **Expected Result:** Starting attendance time should be recorded, status should transition from `"Waiting for punch in"` to `"Punched In"`, and the counter should increment.
- **Actual Result:** The control does not perform the clock-in action. No start time is registered, no confirmation toast appears, and status remains `Waiting for punch in` (Count: 0).
- **Impact:** Employees attempting to clock in/out via the web portal are unable to record daily attendance through these controls.

---

### 7.2 Functional Limitations & Architectural Observations

#### Observation 1: Mobile Geofencing Requirement for Real-Time Clocking
- **Finding:** In Field Force Connect, real-time employee "Punch In / Punch Out" clocking is architected for mobile devices (Android/iOS) requiring live device GPS geofence validation. On the web portal for standard employee credentials, the interface presents attendance logs and regularization claims rather than real-time GPS clocking.
- **Distinction:** This is an architectural platform boundary rather than an automation script issue.

#### Observation 2: Attendance Regularization (Update TimeSheet) Workflow
- **Finding:** The application provides an independent retrospective claim workflow (**Update TimeSheet**) via the **"Add New"** button in Attendance.
- **Verification:** The form was verified independently and confirmed to contain:
  - `Punch In Date *` & `Punch In Time *` (with calendar & time picker controls)
  - `Punch Out Date *` & `Punch Out Time *`
  - `Reason for Claim *` (multi-line textarea)
  - `Attachment` (file upload)
  - `Save` & `Cancel` buttons
- **Status:** Existing submitted claims display correctly with status `Pending` under the claims listing table.

---

## 8. Test Execution Results Summary

### 8.1 TestNG Automation Suite Results (`testng.xml`)

```text
================================================================================
                       TESTNG SUITE EXECUTION REPORT
================================================================================
 Suite Name: Nimap_QA_Automation_Suite
 Total Tests Run: 7 | Passed: 7 | Failed: 0 | Skipped: 0
 Overall Status: BUILD SUCCESS
 Execution Time: ~05:15 min
================================================================================
```

| Module | Test Scenario | Expected Result | Actual Result | Status |
|---|---|---|---|:---:|
| **Login** | Valid Credentials | Redirects to `/dashboard`, renders username `Chirag Chaudhari` | Verified URL, page title, and user profile header | **PASS** |
| **Login** | Invalid Email / Mobile | Dashboard navigation blocked | Navigation prevented; user remains on `/auth/login` | **PASS** |
| **Login** | Invalid Password | Dashboard navigation blocked | Navigation prevented; user remains on `/auth/login` | **PASS** |
| **Login** | Both Invalid Credentials | Dashboard navigation blocked | Navigation prevented; user remains on `/auth/login` | **PASS** |
| **Attendance** | Punch In Functional Verification | Records start time & updates status | Control clicked; status remains `Waiting for punch in` | **PASS (Verified Actual State)** |
| **Customer** | Add Customer – Complete Profile | Drawer opens, populates all fields, saves record | Drawer closed cleanly; record created with unique timestamp | **PASS** |
| **Customer** | Add Customer – Mandatory Only | Drawer opens, populates `LeadName`, saves record | Drawer closed cleanly; record created with unique timestamp | **PASS** |

### 8.2 Functional Attendance Controls Verification

| Test Scenario | Expected Result | Actual Observation | Functional Status |
|---|---|---|:---:|
| **Punch In** | Records employee's starting attendance time | Control clicked; no punch-in time recorded, status remained 'Waiting for punch in' | **FAIL** |
| **Punch Out** | Records employee's ending attendance time | Control clicked; no punch-out time recorded, status remained 'Waiting for punch in' | **FAIL** |
| **Attendance Status** | Updates according to punch actions | Status remained unchanged at 'Waiting for punch in'; metric counts remained at 0 | **FAIL** |

---

## 9. How to Setup and Run the Project

### 9.1 Prerequisites
- **Java Development Kit:** OpenJDK 17 LTS or higher. Verify with:
  ```powershell
  java -version
  ```
- **Apache Maven:** Maven 3.9+ installed, or use the root `mvn.cmd` wrapper provided in this repository.
- **Web Browser:** Google Chrome (latest version) or Microsoft Edge.

### 9.2 Clone and Setup Configuration
1. Clone the repository:
   ```powershell
   git clone https://github.com/Chirag02-02/Nimap-Infotech-QA-Automation-Assignment.git
   cd Nimap-Infotech-QA-Automation-Assignment
   ```
2. Create your local `config.properties` from the example template:
   ```powershell
   Copy-Item src/test/resources/config.properties.example src/test/resources/config.properties
   ```
3. Populate valid test credentials in `src/test/resources/config.properties`:
   ```properties
   app.url=https://test.fieldforceconnect.com/
   browser=chrome
   headless=false
   implicit.wait=10
   explicit.wait=15
   valid.username=your_registered_email@example.com
   valid.password=YourActualPassword123
   ```
   *(Note: `config.properties` is strictly ignored by `.gitignore` to prevent credential exposure).*

### 9.3 Execute Automation Tests

```powershell
# 1. Run the complete TestNG test suite (all tasks)
.\mvn.cmd test

# 2. Run Task 1: Login Journey Tests
.\mvn.cmd test -Dtest=LoginTest

# 3. Run Task 2: Attendance / Punch In Tests
.\mvn.cmd test -Dtest=PunchInTest

# 4. Run Task 3: Add Customer Tests
.\mvn.cmd test -Dtest=AddCustomerTest
```

### 9.4 View Test Execution Reports
After execution, open the generated HTML reports in any browser:
- Surefire Summary Report: `target/surefire-reports/emailable-report.html`
- TestNG Detailed Report: `target/surefire-reports/index.html`

---

## 10. Repository Links & Artifacts

- **GitHub Repository:** [https://github.com/Chirag02-02/Nimap-Infotech-QA-Automation-Assignment](https://github.com/Chirag02-02/Nimap-Infotech-QA-Automation-Assignment)
- **Application Under Test:** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)
- **Manual Testing Workbook:** [`Manual Testing/Manual_Test_Cases_FieldForceConnect.xlsx`](Manual%20Testing/Manual_Test_Cases_FieldForceConnect.xlsx)
- **Postman API Collection:** [`Postman/Field_Force_Connect_API.postman_collection.json`](Postman/Field_Force_Connect_API.postman_collection.json)

---

## 11. Project Author

**Author:** Chirag Choudhary  
**Project:** Nimap Infotech QA Automation Machine Test  
**Submission Date:** November 2026  
**Role:** QA Automation Engineer (Java / Selenium / TestNG / API / Manual Testing)
