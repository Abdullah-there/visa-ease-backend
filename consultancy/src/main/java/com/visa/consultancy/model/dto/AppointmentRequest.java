package com.visa.consultancy.model.dto;



import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentType;
    private String location;
}
