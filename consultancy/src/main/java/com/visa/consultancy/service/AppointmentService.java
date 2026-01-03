package com.visa.consultancy.service;


import com.visa.consultancy.model.dto.AppointmentRequest;
import com.visa.consultancy.model.entity.*;
import com.visa.consultancy.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.visa.consultancy.exception.BusinessRuleException;
import java.time.LocalDate;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ApplicationRepository applicationRepository;
    private final PaymentRepository paymentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              ApplicationRepository applicationRepository,
                              PaymentRepository paymentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.applicationRepository = applicationRepository;
        this.paymentRepository = paymentRepository;
    }

    public Appointment bookAppointment(Long applicationId, AppointmentRequest request) {
        VisaApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new BusinessRuleException(
                "Application not found", org.springframework.http.HttpStatus.NOT_FOUND));

        // Business Rule: Payment must be completed before appointment
        validatePaymentCompletion(applicationId);

        Appointment appointment = new Appointment();
        appointment.setApplication(application);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setLocation(request.getLocation());
        
        return appointmentRepository.save(appointment);
    }

    public java.util.List<Appointment> getAppointmentsByApplication(Long applicationId) {
        return appointmentRepository.findAll().stream()
            .filter(a -> a.getApplication() != null && a.getApplication().getApplicationId().equals(applicationId))
            .toList();
    }

    public Appointment getAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new com.visa.consultancy.exception.BusinessRuleException(
                "Appointment not found", org.springframework.http.HttpStatus.NOT_FOUND));
    }

    private void validatePaymentCompletion(Long applicationId) {
        java.util.List<Payment> payments = paymentRepository.findAll().stream()
            .filter(p -> p.getApplication().getApplicationId().equals(applicationId))
            .toList();
            
        if (payments.isEmpty()) {
            throw new com.visa.consultancy.exception.BusinessRuleException(
                "Payment must be recorded before booking appointment", 
                org.springframework.http.HttpStatus.BAD_REQUEST
            );
        }
        
        boolean allPaid = payments.stream()
            .allMatch(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED);
            
        if (!allPaid) {
            throw new com.visa.consultancy.exception.BusinessRuleException(
                "All payments must be completed before booking appointment", 
                org.springframework.http.HttpStatus.BAD_REQUEST
            );
        }
    }
}
