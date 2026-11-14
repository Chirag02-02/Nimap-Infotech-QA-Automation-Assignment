# Field Force Connect – API Testing & Execution Report
**QA Automation Machine Test | Nimap Infotech**  
**Role:** Senior QA Automation Engineer  
**Date:** November 14, 2026  
**Application Under Test (AUT):** [https://test.fieldforceconnect.com/](https://test.fieldforceconnect.com/)  
**Environment:** `Field Force Connect - QA`

---

## 1. Executive Summary

This report documents the end-to-end API testing execution for the **Field Force Connect** application as part of the Nimap Infotech QA Assessment. API endpoints were identified directly from application network inspection and client application architecture. All test requests have been designed, structured, and validated using Postman conventions with automated assertions, dynamic authentication variable handling, and response schema validations.

---

## 2. Test Environment & Variable Configuration

The Postman Environment **Field Force Connect - QA** has been configured to manage environment variables dynamically across all requests, avoiding hardcoded values and protecting sensitive credentials.

### Configured Environment Variables:

| Variable | Scope | Configured Value (Sanitized) | Purpose |
| :--- | :--- | :--- | :--- |
| `base_url` | Environment | `https://test.fieldforceconnect.com/` | Application root URL |
| `username` | Environment | `chaudharichirag965@gmail.com` | Registered test account email |
| `password` | Environment | `********` | Encrypted/masked test account password |
| `auth_token` | Environment | *(Dynamically populated on Login)* | Base64-encoded Basic Auth token |

---

## 3. Authentication Mechanism & Dynamic Token Handling

### 3.1 Authentication Architecture
- **Login Endpoint:** `POST {{base_url}}api/account/authenticate`
- **Request Format:** JSON (`application/json`)
- **Credentials Payload:**
  ```json
  {
    "username": "{{username}}",
    "password": "{{password}}"
  }
  ```
- **Session & Access Control:**
  Upon successful authentication, the backend validates credentials and returns a complete user profile, role permissions, and company context (`companyId: 1550`, `userId: 1959`).
  Subsequent CRM and Customer endpoints require an `Authorization` header containing the Base64-encoded credentials:
  `Authorization: Basic <base64(username:password)>`

### 3.2 Automated Postman Script for Dynamic Token Storage
Inside the **Tests** tab of the `Login - Valid Credentials` request, the following JavaScript snippet extracts the credentials and sets `auth_token` dynamically in the active environment:

```javascript
pm.test("Status code is 200 OK", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify login succeeds and returns user profile", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.eql(true);
    pm.expect(jsonData.userId).to.be.a("number");
    pm.expect(jsonData.userDetail).to.be.an("object");
});

pm.test("Dynamically extract and set auth_token in environment", function () {
    var username = pm.environment.get("username");
    var password = pm.environment.get("password");
    var basicToken = "Basic " + btoa(username + ":" + password);
    pm.environment.set("auth_token", basicToken);
    pm.expect(pm.environment.get("auth_token")).to.not.be.empty;
});
```

---

## 4. Test Suite Structure & Request Details

The collection is organized into modular folders:

### Folder 1: Authentication

#### 1. Login – Valid Credentials
- **HTTP Method:** `POST`
- **Endpoint:** `{{base_url}}api/account/authenticate`
- **Headers:** `Content-Type: application/json`
- **Payload:** `{"username": "{{username}}", "password": "{{password}}"}`
- **Expected Status:** `200 OK`
- **Validations:**
  - Status code is 200
  - Response time is under 2000 ms
  - `success === true`
  - Valid `userId` and user profile returned
  - `auth_token` set in environment

#### 2. Login – Invalid Credentials
- **HTTP Method:** `POST`
- **Endpoint:** `{{base_url}}api/account/authenticate`
- **Headers:** `Content-Type: application/json`
- **Payload:** `{"username": "invalid_qa_user@nimap.com", "password": "WrongPassword@999"}`
- **Expected Status:** `200 OK` (Application handles authentication denial within JSON payload)
- **Validations:**
  - Status code is 200
  - `success === false`
  - No `userId` or permissions issued

---

### Folder 2: Customer Management

#### 3. GET Customers – Status List
- **HTTP Method:** `GET`
- **Endpoint:** `{{base_url}}api/CRM/LeadStatus`
- **Headers:** `Authorization: {{auth_token}}`
- **Expected Status:** `200 OK`
- **Validations:**
  - Status code is 200
  - `jsonData.Status === 200`
  - `jsonData.Message === "Success"`
  - Status categories list contains `Hot`, `Warm`, and `Cold`

#### 4. GET Customer By ID
- **HTTP Method:** `GET`
- **Endpoint:** `{{base_url}}api/CRM/LeadWithProduct?leadId=4708`
- **Headers:** `Authorization: {{auth_token}}`
- **Expected Status:** `200 OK`
- **Validations:**
  - Status code is 200
  - Returns customer profile object matching `Id: 4708`
  - Valid string returned for `LeadName`

#### 5. POST Customer List (Paged View)
- **HTTP Method:** `POST`
- **Endpoint:** `{{base_url}}api/CRM/Leads`
- **Headers:** `Authorization: {{auth_token}}`, `Content-Type: application/json`
- **Payload:** `{"from": 1, "to": 10}`
- **Expected Status:** `200 OK`
- **Validations:**
  - Status code is 200
  - `jsonData.data.TotalRecords` is a number
  - `jsonData.data.Leads` is an array

#### 6. Add Customer
- **HTTP Method:** `POST`
- **Endpoint:** `{{base_url}}api/CRM/Lead`
- **Headers:** `Authorization: {{auth_token}}`, `Content-Type: application/json`
- **Payload:**
  ```json
  {
    "LeadName": "Nimap QA Customer {{$timestamp}}",
    "PersonName": "Chirag QA",
    "MobileNo": "9820012345",
    "Email": "chirag.qa@example.com",
    "Address": "Mumbai, Maharashtra",
    "StatusId": 1
  }
  ```
- **Expected Status:** `200 OK`
- **Validations:**
  - Status code is 200
  - `jsonData.Status === 200`
  - Response message indicates successful creation

---

## 5. Live Execution Results & Evidence Summary

| Test Case ID | Request Name | Method | Actual HTTP Status | Execution Result | Remarks |
| :--- | :--- | :---: | :---: | :---: | :--- |
| `TC_API_AUTH_001` | Login - Valid Credentials | `POST` | `200 OK` | **PASS** | Authenticated as `Chirag Chaudhari` (`userId: 1959`). |
| `TC_API_AUTH_002` | Login - Invalid Credentials | `POST` | `200 OK` | **PASS** | `{"success": false}` returned. Access rejected. |
| `TC_API_CUST_001` | GET Customer Statuses | `GET` | `200 OK` | **PASS** | Retrieved 4 status definitions (`Hot`, `Warm`, `Cold`, `Success`). |
| `TC_API_CUST_002` | GET Customer By ID | `GET` | `200 OK` | **PASS** | Customer record #4708 (`Apex Sol 41124`) retrieved. |
| `TC_API_CUST_003` | POST Customer List (Paged) | `POST` | `200 OK` | **PASS** | Retrieved active database records (TotalRecords: 14). |
| `TC_API_CUST_004` | Add Customer | `POST` | `Pending` | **Awaiting Approval** | Endpoint & payload verified; pending final approval. |

---

## 6. Defects & Technical Observations Discovered

1. **HTTP Status Code for Authentication Failure:**
   - *Observation:* When invalid credentials are supplied to `POST /api/account/authenticate`, the application server returns an HTTP status of `200 OK` with a body of `{"success": false}` rather than a standard RESTful `401 Unauthorized` or `400 Bad Request`.
   - *Recommendation:* Align error responses with RFC 7235 standards by returning `401 Unauthorized` for authentication failures to assist standard API clients.

2. **Customer Query via HTTP GET vs POST:**
   - *Observation:* While individual customer profiles can be fetched with `GET /api/CRM/LeadWithProduct?leadId={id}`, retrieving the customer list requires a `POST` request to `/api/CRM/Leads` due to pagination and filter parameter handling.
   - *Recommendation:* Support both RESTful `GET /api/CRM/Leads?from=1&to=10` and POST filtering for standardized consumer integration.

---

## 7. Deliverables Index

All required deliverables are located in the `Postman_API_Testing/` folder:
1. `FieldForceConnect_API_Collection.json` – Postman Collection v2.1.0 with assertions and dynamic scripts.
2. `FieldForceConnect_Environment.json` – Postman Environment configuration.
3. `API_Test_Cases.xlsx` – Comprehensive Test Case execution sheet.
4. `API_Execution_Report.md` – This execution and architectural analysis report.
5. `Screenshots/` – Execution evidence and test result captures.
