package com.visa.consultancy.controller;


import com.visa.consultancy.model.dto.AppointmentRequest;
import com.visa.consultancy.model.entity.Appointment;
import com.visa.consultancy.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications/{id}/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@PathVariable Long id, 
                                                      @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(id, request));
    }

    @GetMapping
    public ResponseEntity<java.util.List<com.visa.consultancy.model.entity.Appointment>> getAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByApplication(id));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id, @PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
    }
}
