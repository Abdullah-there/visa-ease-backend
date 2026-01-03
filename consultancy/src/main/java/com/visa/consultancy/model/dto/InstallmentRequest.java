package com.visa.consultancy.model.dto;



import lombok.Data;
import java.time.LocalDate;

@Data
public class InstallmentRequest {
    private Double amount;
    private LocalDate installmentDate;
    private String transactionReference;
}