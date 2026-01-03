package com.visa.consultancy.model.dto;



import lombok.Data;

@Data
public class DocumentRequest {
    private Long documentId;
    private String filePath; // Simulating upload
    private String notes;
}