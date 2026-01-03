# Visa Management API — Phase 1 (CRUD)

This API manages visa applicants, their applications, payments, decisions, documents, and appointments. It supports full CRUD operations aligned with the visa application workflow use cases.

> Base URL: `{{baseUrl}}`  
> Default: `http://localhost:8080`  
> Authentication: Not specified in this phase. If your API uses auth, add the relevant scheme (e.g., Bearer token) to all endpoints.

---

## Environments & Variables

The collection commonly uses the following variables:

- `{{baseUrl}}`  
  The base URL for the API (e.g., `http://localhost:8080`).

- `{{applicantId}}`  
  Unique identifier of an applicant resource (integer, auto-generated).

- `{{applicationId}}`  
  Unique identifier of an application resource (integer, auto-generated).

- `{{paymentId}}`  
  Unique identifier of a payment resource (integer, auto-generated).

- `{{appointmentId}}`  
  Unique identifier of an appointment resource (integer, auto-generated).

- `{{decisionId}}`  
  Unique identifier of a decision resource (integer, auto-generated).

These should be defined in a Postman environment or as collection variables.

---

## Resources Overview

- **Applicants** (VC-SUC001)
  - Create Applicant
  - Get Applicant
  - Update Applicant

- **Applications** (VC-SUC001 → VC-SUC004)
  - Create Application
  - Get Application
  - Update Application
  - Submit Application
  - Record Decision

- **Documents** (VC-SUC002)
  - Add Document to Application
  - Get Documents by Application
  - Get Document Line Item

- **Payments** (VC-SUC003)
  - Record Payment
  - Get Payment
  - Get Payments by Application
  - Add Installment

- **Appointments** (VC-SUC005)
  - Book Appointment
  - Get Appointments by Application
  - Get Appointment

---

## Applicants

### Create Applicant

**Use Case:** VC-SUC001.00 — Visa Application Request

**Request name:** `Create Applicant`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applicants`

Creates a new visa applicant. The applicant must have a valid passport with at least 6 months validity remaining.

**Path parameters:**  
None.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-15",
  "passportNumber": "P123456789",
  "passportExpiryDate": "2027-06-30",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "nationality": "US"
}
```

**Field descriptions:**

- `firstName` (string, required) — Applicant's first name.
- `lastName` (string, required) — Applicant's last name.
- `dateOfBirth` (string, ISO 8601, required) — Date of birth (YYYY-MM-DD).
- `passportNumber` (string, required) — Passport number.
- `passportExpiryDate` (string, ISO 8601, required) — Passport expiry date. Must be valid for at least 6 months from today.
- `email` (string, required) — Email address.
- `phone` (string, required) — Phone number.
- `nationality` (string, required) — Applicant's nationality (ISO country code or full name).

**Responses (actual):**

- `201 Created` — Applicant successfully created. Response body:

  ```json
  {
    "applicantId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-15",
    "passportNumber": "P123456789",
    "passportExpiryDate": "2027-06-30",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "nationality": "US",
    "createdAt": "2025-01-10T10:30:00Z"
  }
  ```

- `400 Bad Request` — Invalid input (e.g., passport expires in < 6 months, missing required field).
- `500 Internal Server Error` — Server-side error.

---

### Get Applicant

**Request name:** `Get Applicant`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applicants/{{applicantId}}`

Fetches details for a single applicant by ID.

**Path parameters:**

- `applicantId` (integer) — ID of the applicant to retrieve.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Returns the applicant resource:

  ```json
  {
    "applicantId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-15",
    "passportNumber": "P123456789",
    "passportExpiryDate": "2027-06-30",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "nationality": "US",
    "createdAt": "2025-01-10T10:30:00Z"
  }
  ```

- `404 Not Found` — No applicant with the given ID exists.
- `500 Internal Server Error` — Server-side error.

---

### Update Applicant

**Request name:** `Update Applicant`  
**Method:** `PUT`  
**URL:** `{{baseUrl}}/applicants/{{applicantId}}`

Updates an existing applicant's details. Validates passport expiry if updated.

**Path parameters:**

- `applicantId` (integer) — ID of the applicant to update.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

Full or partial update. All fields are optional:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-15",
  "passportNumber": "P123456789",
  "passportExpiryDate": "2027-06-30",
  "email": "john.doe+updated@example.com",
  "phone": "+1987654321",
  "nationality": "US"
}
```

**Responses (actual):**

- `200 OK` — Applicant successfully updated; returns updated resource.
- `400 Bad Request` — Validation error (e.g., passport expires in < 6 months).
- `404 Not Found` — Applicant with given ID not found.
- `500 Internal Server Error` — Server-side error.

---

## Applications

### Create Application

**Use Case:** VC-SUC001.00 — Visa Application Request

**Request name:** `Create Application`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications`

Creates a new visa application in DRAFT status. The applicant must exist.

**Path parameters:**  
None.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "applicantId": 1,
  "consultantId": null,
  "visaType": "BUSINESS"
}
```

**Field descriptions:**

- `applicantId` (integer, required) — ID of the applicant for this application.
- `consultantId` (integer, optional) — ID of the visa consultant assigned to this application.
- `visaType` (string, required) — Type of visa (e.g., `BUSINESS`, `TOURIST`, `STUDENT`, `WORK`).

**Responses (actual):**

- `201 Created` — Application successfully created; response:

  ```json
  {
    "applicationId": 1,
    "applicantId": 1,
    "visaType": "BUSINESS",
    "applicationDate": "2025-01-10",
    "status": "DRAFT",
    "isDocumentFileReady": false,
    "createdAt": "2025-01-10T10:35:00Z",
    "updatedAt": "2025-01-10T10:35:00Z"
  }
  ```

- `400 Bad Request` — Invalid data or applicant not found.
- `404 Not Found` — Applicant or consultant not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Application

**Request name:** `Get Application`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}`

Retrieves details of a specific application.

**Path parameters:**

- `applicationId` (integer) — ID of the application to retrieve.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Application data:

  ```json
  {
    "applicationId": 1,
    "applicantId": 1,
    "visaType": "BUSINESS",
    "applicationDate": "2025-01-10",
    "status": "DRAFT",
    "isDocumentFileReady": false,
    "createdAt": "2025-01-10T10:35:00Z",
    "updatedAt": "2025-01-10T10:35:00Z"
  }
  ```

  Status values: `DRAFT`, `SUBMITTED`, `UNDER_REVIEW`, `DOCUMENT_VERIFICATION`, `PAYMENT_PENDING`, `APPOINTMENT_SCHEDULED`, `READY_FOR_INTERVIEW`, `DECISION_PENDING`, `CLOSED`.

- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Update Application

**Request name:** `Update Application`  
**Method:** `PUT`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}`

Updates an existing visa application (typically consultant or visa type).

**Path parameters:**

- `applicationId` (integer) — ID of the application to update.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "applicantId": 1,
  "consultantId": 2,
  "visaType": "TOURIST"
}
```

**Responses (actual):**

- `200 OK` — Application successfully updated; returns updated resource.
- `400 Bad Request` — Invalid payload or validation error.
- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Submit Application

**Use Case:** VC-SUC002.00 — Provides Documents

**Request name:** `Submit Application`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/submit`

Submits an application for processing. Validates that:
- Application is in DRAFT status
- All required documents are ready (`isDocumentFileReady == true`)

Transitions status from `DRAFT` → `SUBMITTED`.

**Path parameters:**

- `applicationId` (integer) — ID of the application to submit.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None (empty request body).

**Responses (actual):**

- `200 OK` — Application successfully submitted; returns updated status:

  ```json
  {
    "applicationId": 1,
    "applicantId": 1,
    "visaType": "BUSINESS",
    "applicationDate": "2025-01-10",
    "status": "SUBMITTED",
    "isDocumentFileReady": true,
    "createdAt": "2025-01-10T10:35:00Z",
    "updatedAt": "2025-01-10T11:00:00Z"
  }
  ```

- `400 Bad Request` — Application not in DRAFT status or documents not ready.
- `404 Not Found` — Application not found.
- `409 Conflict` — Application already submitted or finalized.
- `500 Internal Server Error` — Server-side error.

---

## Documents

### Add Document to Application

**Use Case:** VC-SUC002.00 — Provides Documents

**Request name:** `Add Document to Application`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/documents`

Uploads or attaches a required document to an application. The document template must exist in the system.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "documentId": 1,
  "filePath": "/uploads/passport_copy_2025.pdf",
  "notes": "Color copy of passport front and back"
}
```

**Field descriptions:**

- `documentId` (integer, required) — ID of the document template (e.g., 1 for Passport, 2 for Visa Interview Appointment).
- `filePath` (string, required) — File path or URL where the document is stored.
- `notes` (string, optional) — Additional notes about the document (e.g., "Certified copy").

**Responses (actual):**

- `201 Created` — Document successfully added:

  ```json
  {
    "lineItemId": 1,
    "applicationId": 1,
    "documentId": 1,
    "filePath": "/uploads/passport_copy_2025.pdf",
    "notes": "Color copy of passport front and back",
    "uploadDate": "2025-01-10T11:15:00Z"
  }
  ```

- `400 Bad Request` — Invalid document ID or application.
- `404 Not Found` — Application or document template not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Documents by Application

**Request name:** `Get Documents by Application`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/documents`

Lists all documents attached to an application.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — List of document line items:

  ```json
  [
    {
      "lineItemId": 1,
      "applicationId": 1,
      "documentId": 1,
      "filePath": "/uploads/passport_copy_2025.pdf",
      "notes": "Color copy of passport front and back",
      "uploadDate": "2025-01-10T11:15:00Z"
    },
    {
      "lineItemId": 2,
      "applicationId": 1,
      "documentId": 2,
      "filePath": "/uploads/appointment_letter.pdf",
      "notes": "Interview appointment confirmation from VAC",
      "uploadDate": "2025-01-10T11:20:00Z"
    }
  ]
  ```

- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Document Line Item

**Request name:** `Get Document Line Item`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/documents/{{documentLineItemId}}`

Retrieves a specific document attachment.

**Path parameters:**

- `documentLineItemId` (integer) — ID of the document line item.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Document line item details.
- `404 Not Found` — Document line item not found.
- `500 Internal Server Error` — Server-side error.

---

## Payments

### Record Payment

**Use Case:** VC-SUC003.00 — Pays Consultant, Application and Embassy Fees

**Request name:** `Record Payment`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/payments`

Records a payment (consultant fee, application fee, embassy fee, or installment). Applicant and application must exist.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "applicantId": 1,
  "amount": 150.00,
  "paymentType": "CONSULTANT_FEE",
  "dueDate": "2025-02-10"
}
```

**Field descriptions:**

- `applicantId` (integer, required) — ID of the applicant making the payment.
- `amount` (number, required) — Payment amount (e.g., 150.00).
- `paymentType` (string, required) — Type of fee: `CONSULTANT_FEE`, `APPLICATION_FEE`, `EMBASSY_FEE`, `INSTALLMENT`.
- `dueDate` (string, ISO 8601, required) — Due date for the payment (YYYY-MM-DD).

**Responses (actual):**

- `201 Created` — Payment successfully recorded:

  ```json
  {
    "paymentId": 1,
    "applicationId": 1,
    "applicantId": 1,
    "amount": 150.00,
    "paymentType": "CONSULTANT_FEE",
    "status": "PENDING",
    "dueDate": "2025-02-10",
    "createdAt": "2025-01-10T11:30:00Z"
  }
  ```

  Status values: `PENDING`, `PARTIAL`, `COMPLETED`, `OVERDUE`.

- `400 Bad Request` — Invalid payment data.
- `404 Not Found` — Application or applicant not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Payment

**Request name:** `Get Payment`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/payments/{{paymentId}}`

Retrieves details for a specific payment by ID.

**Path parameters:**

- `paymentId` (integer) — ID of the payment to retrieve.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Payment resource:

  ```json
  {
    "paymentId": 1,
    "applicationId": 1,
    "applicantId": 1,
    "amount": 150.00,
    "paymentType": "CONSULTANT_FEE",
    "status": "COMPLETED",
    "dueDate": "2025-02-10",
    "createdAt": "2025-01-10T11:30:00Z"
  }
  ```

- `404 Not Found` — Payment not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Payments by Application

**Request name:** `Get Payments by Application`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/payments`

Lists all payments for a given application.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — List of payment records:

  ```json
  [
    {
      "paymentId": 1,
      "applicationId": 1,
      "applicantId": 1,
      "amount": 150.00,
      "paymentType": "CONSULTANT_FEE",
      "status": "COMPLETED",
      "dueDate": "2025-02-10",
      "createdAt": "2025-01-10T11:30:00Z"
    },
    {
      "paymentId": 2,
      "applicationId": 1,
      "applicantId": 1,
      "amount": 500.00,
      "paymentType": "EMBASSY_FEE",
      "status": "PENDING",
      "dueDate": "2025-02-15",
      "createdAt": "2025-01-10T11:32:00Z"
    }
  ]
  ```

- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Add Installment

**Request name:** `Add Installment`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/payments/{{paymentId}}/installments`

Records an installment payment (partial payment for a fee).

**Path parameters:**

- `applicationId` (integer) — ID of the application.
- `paymentId` (integer) — ID of the payment to which this installment belongs.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "amount": 75.00,
  "installmentDate": "2025-01-20",
  "transactionReference": "TXN-20250120-001"
}
```

**Field descriptions:**

- `amount` (number, required) — Installment amount.
- `installmentDate` (string, ISO 8601, required) — Date of the installment payment (YYYY-MM-DD).
- `transactionReference` (string, optional) — Transaction/receipt reference number.

**Responses (actual):**

- `201 Created` — Installment successfully recorded:

  ```json
  {
    "installmentId": 1,
    "paymentId": 1,
    "amount": 75.00,
    "installmentDate": "2025-01-20",
    "transactionReference": "TXN-20250120-001",
    "createdAt": "2025-01-10T11:35:00Z"
  }
  ```

- `400 Bad Request` — Invalid installment data.
- `404 Not Found` — Payment not found.
- `500 Internal Server Error` — Server-side error.

---

## Appointments

### Book Appointment

**Use Case:** VC-SUC005.00 — Books Appointment for VAC and Interview

**Request name:** `Book Appointment`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/appointments`

Books a biometric (UK, Canada) or interview appointment (USA). Validates that all payments are completed before booking.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "appointmentDate": "2025-02-15",
  "appointmentTime": "10:30",
  "appointmentType": "BIOMETRIC",
  "location": "VAC London - 123 High Street"
}
```

**Field descriptions:**

- `appointmentDate` (string, ISO 8601, required) — Appointment date (YYYY-MM-DD).
- `appointmentTime` (string, HH:mm, required) — Appointment time in 24-hour format.
- `appointmentType` (string, required) — Type: `BIOMETRIC`, `INTERVIEW`.
- `location` (string, required) — Location/VAC center name.

**Responses (actual):**

- `201 Created` — Appointment successfully booked:

  ```json
  {
    "appointmentId": 1,
    "applicationId": 1,
    "appointmentDate": "2025-02-15",
    "appointmentTime": "10:30",
    "appointmentType": "BIOMETRIC",
    "location": "VAC London - 123 High Street",
    "createdAt": "2025-01-10T11:40:00Z"
  }
  ```

- `400 Bad Request` — Appointment data invalid or payments not completed.
- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Appointments by Application

**Request name:** `Get Appointments by Application`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/appointments`

Lists all appointments for a given application.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — List of appointments:

  ```json
  [
    {
      "appointmentId": 1,
      "applicationId": 1,
      "appointmentDate": "2025-02-15",
      "appointmentTime": "10:30",
      "appointmentType": "BIOMETRIC",
      "location": "VAC London - 123 High Street",
      "createdAt": "2025-01-10T11:40:00Z"
    }
  ]
  ```

- `404 Not Found` — Application not found.
- `500 Internal Server Error` — Server-side error.

---

### Get Appointment

**Request name:** `Get Appointment`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/appointments/{{appointmentId}}`

Retrieves a specific appointment.

**Path parameters:**

- `applicationId` (integer) — ID of the application.
- `appointmentId` (integer) — ID of the appointment to retrieve.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Appointment details.
- `404 Not Found` — Appointment not found.
- `500 Internal Server Error` — Server-side error.

---

## Decisions

### Record Decision

**Use Case:** VC-SUC007.00 — Visa Issuance or Refusal

**Request name:** `Record Decision`  
**Method:** `POST`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/decision`

Records an embassy decision (approval or refusal) for an application. If approved, creates a `Visa` record; if refused, creates a `RefusalLetter` record.

**Path parameters:**

- `applicationId` (integer) — ID of the application.

**Query parameters:**  
None.

**Headers:**

- `Content-Type: application/json`

**Request body:**

```json
{
  "decisionType": "ISSUED",
  "decisionNotes": "Application approved. Visa issued for 1 year.",
  "visaNumber": "GB2025001234",
  "refusalReason": null
}
```

Or for refusal:

```json
{
  "decisionType": "REFUSED",
  "decisionNotes": "Application rejected due to insufficient documentation.",
  "visaNumber": null,
  "refusalReason": "Incomplete supporting documents. Applicant may appeal within 30 days."
}
```

**Field descriptions:**

- `decisionType` (string, required) — `ISSUED` or `REFUSED`.
- `decisionNotes` (string, required) — Notes about the decision.
- `visaNumber` (string, conditional) — Visa number if decision is `ISSUED`; null if `REFUSED`.
- `refusalReason` (string, conditional) — Reason for refusal if decision is `REFUSED`; null if `ISSUED`.

**Responses (actual):**

- `201 Created` — Decision successfully recorded:

  ```json
  {
    "decisionId": 1,
    "applicationId": 1,
    "decisionType": "ISSUED",
    "decisionNotes": "Application approved. Visa issued for 1 year.",
    "createdAt": "2025-01-15T14:00:00Z"
  }
  ```

- `400 Bad Request` — Invalid decision payload or business rule violation.
- `404 Not Found` — Application not found.
- `409 Conflict` — Decision already exists for this application.
- `500 Internal Server Error` — Server-side error.

---

### Get Decision

**Request name:** `Get Decision`  
**Method:** `GET`  
**URL:** `{{baseUrl}}/applications/{{applicationId}}/decision/{{decisionId}}`

Retrieves a specific decision record.

**Path parameters:**

- `applicationId` (integer) — ID of the application.
- `decisionId` (integer) — ID of the decision.

**Query parameters:**  
None.

**Headers:**  
Not required.

**Request body:**  
None.

**Responses (actual):**

- `200 OK` — Decision details.
- `404 Not Found` — Decision not found.
- `500 Internal Server Error` — Server-side error.

---

## Error Handling

All endpoints follow a consistent error response format:

```json
{
  "timestamp": "2025-01-10T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Passport must be valid for at least 6 months"
}
```

**Common HTTP Status Codes:**

- `200 OK` — Request succeeded.
- `201 Created` — Resource successfully created.
- `400 Bad Request` — Invalid input or business rule violation.
- `404 Not Found` — Resource not found.
- `409 Conflict` — Resource state conflict (e.g., decision already exists).
- `500 Internal Server Error` — Server error.

---

## Use Case Alignment

| Use Case | Endpoints |
|----------|-----------|
| **VC-SUC001** — Visa Application Request | POST /applicants, GET /applicants/{id}, POST /applications, GET /applications/{id}, PUT /applications/{id} |
| **VC-SUC002** — Provides Documents | POST /applications/{id}/documents, GET /applications/{id}/documents, POST /applications/{id}/submit |
| **VC-SUC003** — Pays Consultant, Application and Embassy Fees | POST /applications/{id}/payments, GET /applications/{id}/payments, GET /payments/{id}, POST /applications/{id}/payments/{paymentId}/installments |
| **VC-SUC004** — Makes documented file | Covered by document endpoints + submit |
| **VC-SUC005** — Books Appointment for VAC and Interview | POST /applications/{id}/appointments, GET /applications/{id}/appointments, GET /applications/{id}/appointments/{id} |
| **VC-SUC006** — Interview (Embassy) | GET /applications/{id} (status tracking) |
| **VC-SUC007** — Visa Issuance or Refusal | POST /applications/{id}/decision, GET /applications/{id}/decision/{id} |

---

## Notes

- **Validation:** Passport expiry is validated at applicant creation/update (must be valid ≥6 months). Applications cannot be submitted without documents ready and in DRAFT status. Appointments cannot be booked until all payments are completed.
- **Status Progression:** Application status flows: DRAFT → SUBMITTED → UNDER_REVIEW → DOCUMENT_VERIFICATION → PAYMENT_PENDING → APPOINTMENT_SCHEDULED → READY_FOR_INTERVIEW → DECISION_PENDING → CLOSED.
- **Timestamps:** All timestamps are in ISO 8601 format (UTC).
- **IDs:** All resource IDs are auto-generated integers.
- **No Authentication:** This Phase 1 API does not include authentication/authorization. Add Bearer token support in Phase 2.

