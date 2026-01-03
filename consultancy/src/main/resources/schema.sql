-- PostgreSQL Schema for Visa Management System

CREATE TABLE visa_applicant (
    applicant_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_number VARCHAR(50) UNIQUE NOT NULL,
    passport_expiry_date DATE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    phone VARCHAR(50),
    nationality VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE visa_consultant (
    consultant_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    specialization VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE visa_application (
    application_id BIGSERIAL PRIMARY KEY,
    applicant_id BIGINT NOT NULL REFERENCES visa_applicant(applicant_id),
    consultant_id BIGINT REFERENCES visa_consultant(consultant_id),
    visa_type VARCHAR(100) NOT NULL,
    application_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(50) DEFAULT 'DRAFT',
    is_document_file_ready BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document (
    document_id BIGSERIAL PRIMARY KEY,
    document_name VARCHAR(255) NOT NULL,
    document_type VARCHAR(100) NOT NULL,
    description TEXT,
    is_required BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document_line_item (
    line_item_id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL REFERENCES visa_application(application_id),
    document_id BIGINT NOT NULL REFERENCES document(document_id),
    file_path TEXT,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verification_status VARCHAR(50) DEFAULT 'PENDING',
    verified_by BIGINT REFERENCES visa_consultant(consultant_id),
    notes TEXT,
    UNIQUE(application_id, document_id)
);

CREATE TABLE payment (
    payment_id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL REFERENCES visa_application(application_id),
    applicant_id BIGINT NOT NULL REFERENCES visa_applicant(applicant_id),
    amount DECIMAL(10, 2) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_installment (
    installment_id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL REFERENCES payment(payment_id),
    amount DECIMAL(10, 2) NOT NULL,
    installment_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    transaction_reference VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE appointment (
    appointment_id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL REFERENCES visa_application(application_id),
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    appointment_type VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE visa_decision (
    decision_id BIGSERIAL PRIMARY KEY,
    application_id BIGINT UNIQUE NOT NULL REFERENCES visa_application(application_id),
    decision_date DATE NOT NULL DEFAULT CURRENT_DATE,
    decision_type VARCHAR(50) NOT NULL, -- 'ISSUED' or 'REFUSED'
    decision_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE visa (
    visa_id BIGSERIAL PRIMARY KEY,
    decision_id BIGINT UNIQUE NOT NULL REFERENCES visa_decision(decision_id),
    visa_number VARCHAR(100) UNIQUE NOT NULL,
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    entries VARCHAR(50),
    duration_of_stay VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refusal_letter (
    refusal_id BIGSERIAL PRIMARY KEY,
    decision_id BIGINT UNIQUE NOT NULL REFERENCES visa_decision(decision_id),
    refusal_date DATE NOT NULL DEFAULT CURRENT_DATE,
    refusal_reason TEXT NOT NULL,
    appeal_deadline DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_application_applicant ON visa_application(applicant_id);
CREATE INDEX idx_application_consultant ON visa_application(consultant_id);
CREATE INDEX idx_payment_application ON payment(application_id);
CREATE INDEX idx_appointment_application ON appointment(application_id);
CREATE INDEX idx_decision_application ON visa_decision(application_id);