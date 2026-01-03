package com.visa.consultancy.model.dto;


import lombok.Data;

@Data
public class DecisionRequest {
    private String decisionType; // "ISSUED" or "REFUSED"
    private String decisionNotes;
    private String visaNumber;
    private String refusalReason;
}
