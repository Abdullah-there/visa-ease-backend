package com.visa.consultancy.controller;



import com.visa.consultancy.model.dto.PaymentRequest;
import com.visa.consultancy.model.dto.InstallmentRequest;
import com.visa.consultancy.model.entity.Payment;
import com.visa.consultancy.model.entity.PaymentInstallment;
import com.visa.consultancy.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/applications/{id}/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> recordPayment(@PathVariable Long id, 
                                                @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.recordPayment(id, request));
    }

    @PostMapping("/{paymentId}/installments")
    public ResponseEntity<PaymentInstallment> addInstallment(@PathVariable Long id,
                                                            @PathVariable Long paymentId,
                                                            @Valid @RequestBody InstallmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addInstallment(paymentId, request));
    }

    @GetMapping
    public ResponseEntity<java.util.List<Payment>> getPaymentsByApplication(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentsByApplication(id));
    }
}
