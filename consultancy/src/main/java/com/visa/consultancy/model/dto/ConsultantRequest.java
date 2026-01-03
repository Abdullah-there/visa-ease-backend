package com.visa.consultancy.model.dto;



import lombok.Data;

@Data
public class ConsultantRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isActive;
    private String specialization;
    private String password;
}
