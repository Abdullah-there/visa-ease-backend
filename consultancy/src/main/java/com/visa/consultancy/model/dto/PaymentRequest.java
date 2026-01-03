package com.visa.consultancy.model.dto;



import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    private Long applicantId;
    private Double amount;
    private String paymentType;
    private LocalDate dueDate;
}
