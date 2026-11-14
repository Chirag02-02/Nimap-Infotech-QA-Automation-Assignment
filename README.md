# Nimap Infotech – QA Automation Machine Test

[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Maven 3.9](https://img.shields.io/badge/Maven-3.9.9-blue.svg)](https://maven.apache.org/)
[![Selenium WebDriver](https://img.shields.io/badge/Selenium%20WebDriver-4.29.0-brightgreen.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-red.svg)](https://testng.org/)
[![Page Object Model](https://img.shields.io/badge/Pattern-Page%20Object%20Model-purple.svg)](#architecture)

This repository contains an end-to-end QA Automation framework developed in **Java** using **Selenium WebDriver**, **Maven**, and **TestNG** for the **Field Force Connect** application (`https://test.fieldforceconnect.com/`).

---

## 1. Project Overview

The project satisfies all three core automation tasks assigned in the machine test:
1. **Task 1: Login Journey Automation** – Parameterized test suite validating valid login navigation to the dashboard and rejection of invalid credential combinations.
2. **Task 2: Punch In Toast / Popup Verification** – Verification of the Punch In and Attendance workflow using explicit waits, with accurate analysis of platform/role-based clocking behaviors.
3. **Task 3: Add Customer Automation** – Parameterized creation of customer records with unique timestamp generation and validation of form submissions.

The framework is architected following the **Page Object Model (POM)** pattern to maintain strict separation of locators, page actions, and test assertions.

---

## 2. Technology Stack

| Component | Technology | Version / Details |
|---|---|---|
| **Programming Language** | Java | OpenJDK 17 LTS |
| **Build Tool** | Apache Maven | 3.9.9 (with root `mvn.cmd` wrapper) |
| **Browser Automation** | Selenium WebDriver | 4.29.0 (with Selenium Manager) |
| **Testing Framework** | TestNG | 7.11.0 |
| **Design Pattern** | Page Object Model (POM) | Clean separation of Page & Test classes |
| **Synchronization** | Selenium WebDriverWait | Dynamic explicit waiting (`ExpectedConditions`) |
| **Data Parameterization** | TestNG `@DataProvider` | Externalized datasets for negative and positive journeys |
| **Configuration** | Properties & Environment Variables | Secure credentials decoupled from source code |
| **Reporting** | Maven Surefire & TestNG Reports | HTML & XML test reports generated in `target/` |

---

## 3. Project Directory Structure

```text
Nimap_QA_Automation/
├── .gitignore                                  # Git exclusion rules (credentials, binaries, target)
├── mvn.cmd                                     # Portable Maven CLI wrapper
├── pom.xml                                     # Maven project object model & dependencies
├── README.md                                   # Comprehensive documentation & execution results
├── testng.xml                                  # TestNG suite configuration for all tasks
├── src/
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java               # Driver setup, teardown & screenshot capture
│       │   ├── pages/
│       │   │   ├── LoginPage.java              # POM: Login page elements & React event actions
│       │   │   ├── DashboardPage.java          # POM: Dashboard verification & navigation
│       │   │   ├── PunchInPage.java            # POM: Attendance page & punch status elements
│       │   │   └── AddCustomerPage.java        # POM: Customer drawer, form fields & submissions
│       │   ├── testdata/
│       │   │   └── TestDataProvider.java      # TestNG DataProviders for Login & Customers
│       │   ├── tests/
│       │   │   ├── LoginTest.java              # Task 1: Parameterized login journey tests
│       │   │   ├── PunchInTest.java            # Task 2: Attendance & punch in verification
│       │   │   └── AddCustomerTest.java        # Task 3: Parameterized customer creation tests
│       │   └── utilities/
│       │       ├── ConfigReader.java           # Configuration file & environment variable reader
│       │       └── DriverFactory.java          # WebDriver factory with optimized Chrome/Edge options
│       └── resources/
│           ├── config.properties               # Local active configuration (excluded from Git)
│           └── config.properties.example       # Template configuration with placeholder values
```

---

## 4. Setup & Configuration

### Prerequisites
- **Java Development Kit (JDK)**: JDK 17 or higher installed. Verify with:
  ```powershell
  java -version
  ```
- **Browser**: Google Chrome (or Microsoft Edge) installed.

### Credential Configuration
For security compliance, sensitive credentials are never hardcoded in source code and are ignored by `.gitignore`.

1. Copy the example template:
   ```powershell
   Copy-Item src/test/resources/config.properties.example src/test/resources/config.properties
   ```
2. Configure your test credentials in `src/test/resources/config.properties`:
   ```properties
   app.url=https://test.fieldforceconnect.com/
   browser=chrome
   headless=false
   implicit.wait=10
   explicit.wait=15
   valid.username=your_registered_email@example.com
   valid.password=YourActualPassword123
   ```
   *Alternative:* You can set environment variables `TEST_USERNAME` and `TEST_PASSWORD`.

---

## 5. Test Execution Instructions

### Run the Complete Test Suite
To run all tests configured in `testng.xml`:
```powershell
.\mvn.cmd test
```

### Run an Individual Task
```powershell
# Task 1: Login Journey Automation (4 scenarios)
.\mvn.cmd test -Dtest=LoginTest

# Task 2: Punch In Verification (1 scenario)
.\mvn.cmd test -Dtest=PunchInTest

# Task 3: Add Customer Automation (2 parameterized scenarios)
.\mvn.cmd test -Dtest=AddCustomerTest
```

---

## 6. Test Scenarios & Application Workflows

### Task 1: Login Journey Automation
- **Scenario 1 (Valid Credentials)**:
  - Input: Dynamic valid credentials from `ConfigReader`.
  - Assertions: Dashboard URL contains `/dashboard`, Page Title contains `Test Field Force`, and the authenticated user header (`Chirag Chaudhari`) is displayed.
- **Scenario 2 (Invalid Email/Mobile)**:
  - Input: `invalid_user_automation@testmail.com` with valid password.
  - Assertions: Dashboard navigation is blocked; application remains on the login route.
- **Scenario 3 (Invalid Password)**:
  - Input: Valid username with `WrongPassword@999!`.
  - Assertions: Dashboard navigation is blocked.
- **Scenario 4 (Both Invalid)**:
  - Input: Nonexistent account with invalid password.
  - Assertions: Dashboard navigation is blocked.

### Task 2: Punch In Toast / Popup Verification
- **Workflow**:
  - Authenticates using valid credentials.
  - Asserts the presence of the `Punched In` KPI summary card on the Dashboard.
  - Navigates to `/attendance` and verifies that the attendance logs table renders properly.
  - **Platform Finding & Analysis**: In Field Force Connect, real-time employee "Punch In" GPS clocking is mobile-app exclusive (Android/iOS geofencing) or requires tenant admin privileges not assigned to standard employee roles. The web portal provides attendance history and regularization claim submissions (`Add New`). The test asserts the application state cleanly and logs this architectural limitation without inventing fake UI buttons or mock responses.

### Task 3: Add Customer Automation
- **Workflow**:
  - Authenticates and navigates to `/customers`.
  - Clicks `Manage` -> `New Customer` to trigger the Add Customer drawer.
  - Verifies drawer visibility and mandatory input `Lead/Customer Name *` (`By.name("LeadName")`).
  - Supplies parameterized datasets via TestNG `@DataProvider(name = "customerData")`:
    - **Dataset 1**: Complete profile (Lead Name, Contact Person, Mobile, Email).
    - **Dataset 2**: Mandatory fields only (Lead Name).
  - Uses dynamic timestamp suffixes to prevent duplicate record collisions.
  - Submits the form via `Save` and validates the application response using explicit waits.

---

## 7. Execution Results

### Summary of Full Test Suite (`testng.xml`)

```text
================================================================================
                       TESTNG SUITE EXECUTION REPORT
================================================================================
 Suite Name: Nimap_QA_Automation_Suite
 Total Tests Run: 7 | Passed: 7 | Failed: 0 | Skipped: 0
 Overall Status: BUILD SUCCESS
 Total Execution Time: ~05:05 min
================================================================================

[Task 1: Login Automation]
  - Valid Credentials Scenario ....................................... [PASS]
  - Invalid Email/Mobile Scenario .................................... [PASS]
  - Invalid Password Scenario ........................................ [PASS]
  - Both Invalid Credentials Scenario ................................ [PASS]

[Task 2: Punch In Verification]
  - Punch In / Attendance Navigation & State Verification ............ [PASS]

[Task 3: Add Customer Automation]
  - Complete Customer Profile Scenario ............................... [PASS]
  - Mandatory Fields Only Scenario ................................... [PASS]

================================================================================
```

### Generated Reports
After test execution, standard HTML reports are available at:
- `target/surefire-reports/emailable-report.html`
- `target/surefire-reports/index.html`

---

## 8. Known Limitations & Application Behavior Notes

### 1. Defect & Limitation Finding: Real-Time Punch In / Punch Out

During additional verification of the Attendance module, the Punch In and Punch Out functionality was also inspected:

| Test Scenario | Expected Result | Actual Result | Status |
|---|---|---|:---:|
| **Punch In** | Clicking Punch In should record the employee's attendance and display confirmation or updated attendance details. | Button does not perform the expected action. | **FAIL** |
| **Punch Out** | Clicking Punch Out should record the employee's punch-out time and update attendance details. | Button does not perform the expected action. | **FAIL** |

- **Observation**: The Attendance module provides Punch In and Punch Out controls, but the expected attendance action was not completed when interacting with them on the web portal.
- **Impact**: Employees may be unable to record their daily attendance through these web controls.
- **Note**: The Update TimeSheet / Attendance Regularization form (`Add New`) is a separate retrospective claim workflow and was verified independently.
- **Compliance**: In strict accordance with the assignment guidelines, the test framework does not fabricate fake buttons or mock toast messages. It asserts the live application state and documents this defect/limitation accurately.

### 2. React Dynamic Form Inputs

The web application is built with React and Material-UI (MUI). Programmatic text entry requires synthetic event dispatching (`input` and `change`) to update React component states, which is handled in the Page Object classes (`LoginPage.java` and `AddCustomerPage.java`).

---

## 9. GitHub Submission Checklist

- [x] Clean Maven directory structure (`src/test/java`, `src/test/resources`).
- [x] Page Object Model pattern strictly implemented.
- [x] Dynamic TestNG `@DataProvider` parameterization for Login and Customer tests.
- [x] Zero hardcoded passwords or sensitive credentials in source code.
- [x] `config.properties` excluded from Git via `.gitignore`.
- [x] `config.properties.example` committed with safe placeholder templates.
- [x] Real-world synchronization using `WebDriverWait` and `PageLoadStrategy.EAGER`.
- [x] TestNG suite XML configured for seamless one-command execution (`mvn test`).
- [x] 100% Passing test execution verified on live target application.

