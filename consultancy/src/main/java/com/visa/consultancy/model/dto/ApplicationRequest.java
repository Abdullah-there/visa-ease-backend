package com.visa.consultancy.model.dto;



import lombok.Data;

@Data
public class ApplicationRequest {
    private Long applicantId;
    private Long consultantId;
    private String visaType;
}
