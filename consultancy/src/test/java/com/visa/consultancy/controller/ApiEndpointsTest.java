package com.visa.consultancy.controller;

import com.visa.consultancy.model.dto.ApplicantRequest;
import com.visa.consultancy.model.dto.ApplicationRequest;
import com.visa.consultancy.model.dto.PaymentRequest;
import com.visa.consultancy.model.entity.Payment;
import com.visa.consultancy.model.entity.VisaApplication;
import com.visa.consultancy.model.entity.VisaApplicant;
import com.visa.consultancy.service.ApplicationService;
import com.visa.consultancy.service.ApplicantService;
import com.visa.consultancy.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiEndpointsTest {

    @Test
    void createApplicant_returnsCreated() {
        ApplicantRequest req = new ApplicantRequest();
        req.setFirstName("Alice");
        req.setLastName("Smith");
        req.setDateOfBirth(LocalDate.of(1990,1,1));
        req.setPassportNumber("P111");
        req.setPassportExpiryDate(LocalDate.now().plusYears(2));
        req.setEmail("alice@example.com");
        req.setPhone("+100");
        req.setNationality("US");

        ApplicantService fakeService = new ApplicantService(null) {
            @Override
            public VisaApplicant createApplicant(ApplicantRequest request) {
                VisaApplicant v = new VisaApplicant();
                v.setApplicantId(1L);
                v.setFirstName(request.getFirstName());
                return v;
            }
        };

        ApplicantController controller = new ApplicantController(fakeService);
        ResponseEntity<VisaApplicant> resp = controller.createApplicant(req);
        assertEquals(201, resp.getStatusCode().value());
        assertEquals(1L, resp.getBody().getApplicantId());
    }

    @Test
    void createApplication_returnsCreated() {
        ApplicationRequest req = new ApplicationRequest();
        req.setApplicantId(1L);
        req.setVisaType("BUSINESS");

        ApplicationService fakeService = new ApplicationService(null, null, null) {
            @Override
            public VisaApplication createApplication(ApplicationRequest request) {
                VisaApplication a = new VisaApplication();
                a.setApplicationId(2L);
                a.setVisaType(request.getVisaType());
                return a;
            }
        };

        ApplicationController controller = new ApplicationController(fakeService);
        ResponseEntity<VisaApplication> resp = controller.createApplication(req);
        assertEquals(201, resp.getStatusCode().value());
        assertEquals(2L, resp.getBody().getApplicationId());
    }

    @Test
    void submitApplication_returnsOk() {
        ApplicationService fakeService = new ApplicationService(null, null, null) {
            @Override
            public VisaApplication submitApplication(Long applicationId) {
                VisaApplication a = new VisaApplication();
                a.setApplicationId(applicationId);
                a.setStatus(VisaApplication.ApplicationStatus.SUBMITTED);
                return a;
            }
        };

        ApplicationController controller = new ApplicationController(fakeService);
        ResponseEntity<VisaApplication> resp = controller.submitApplication(3L);
        assertEquals(200, resp.getStatusCode().value());
        assertEquals("SUBMITTED", resp.getBody().getStatus().name());
    }

    @Test
    void recordAndGetPayment() {
        PaymentService fakePaymentService = new PaymentService(null, null, null, null) {
            @Override
            public Payment recordPayment(Long applicationId, PaymentRequest request) {
                Payment p = new Payment();
                p.setPaymentId(5L);
                p.setAmount(request.getAmount());
                return p;
            }

            @Override
            public Payment getPayment(Long paymentId) {
                Payment p = new Payment();
                p.setPaymentId(paymentId);
                p.setAmount(100.0);
                return p;
            }
        };

        PaymentController paymentController = new PaymentController(fakePaymentService);
        PaymentLookupController lookup = new PaymentLookupController(fakePaymentService);

        PaymentRequest req = new PaymentRequest();
        req.setApplicantId(1L);
        req.setAmount(100.0);
        req.setPaymentType("CONSULTANT_FEE");

        ResponseEntity<Payment> createResp = paymentController.recordPayment(1L, req);
        assertEquals(201, createResp.getStatusCode().value());
        assertEquals(5L, createResp.getBody().getPaymentId());

        ResponseEntity<Payment> getResp = lookup.getPayment(5L);
        assertEquals(200, getResp.getStatusCode().value());
        assertEquals(5L, getResp.getBody().getPaymentId());
    }
}
