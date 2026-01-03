package com.visa.consultancy.model.dto;



import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicantRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private LocalDate passportExpiryDate;
    private String email;
    private String phone;
    private String nationality;
    private String password;
}
