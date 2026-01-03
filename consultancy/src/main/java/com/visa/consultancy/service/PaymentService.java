package com.visa.consultancy.service;



import com.visa.consultancy.model.dto.PaymentRequest;
import com.visa.consultancy.model.dto.InstallmentRequest;
import com.visa.consultancy.model.entity.*;
import com.visa.consultancy.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.visa.consultancy.exception.BusinessRuleException;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentInstallmentRepository installmentRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentInstallmentRepository installmentRepository,
                          ApplicationRepository applicationRepository,
                          ApplicantRepository applicantRepository) {
        this.paymentRepository = paymentRepository;
        this.installmentRepository = installmentRepository;
        this.applicationRepository = applicationRepository;
        this.applicantRepository = applicantRepository;
    }

    public Payment recordPayment(Long applicationId, PaymentRequest request) {
        VisaApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new BusinessRuleException(
                "Application not found", org.springframework.http.HttpStatus.NOT_FOUND));

        VisaApplicant applicant = applicantRepository.findById(request.getApplicantId())
            .orElseThrow(() -> new BusinessRuleException(
                "Applicant not found", org.springframework.http.HttpStatus.NOT_FOUND));

        Payment payment = new Payment();
        payment.setApplication(application);
        payment.setApplicant(applicant);
        payment.setAmount(request.getAmount());
        payment.setPaymentType(request.getPaymentType());
        payment.setDueDate(request.getDueDate());
        
        return paymentRepository.save(payment);
    }

    public java.util.List<Payment> getPaymentsByApplication(Long applicationId) {
        return paymentRepository.findAll().stream()
            .filter(p -> p.getApplication() != null && p.getApplication().getApplicationId().equals(applicationId))
            .toList();
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new BusinessRuleException(
                "Payment not found", org.springframework.http.HttpStatus.NOT_FOUND));
    }

    public PaymentInstallment addInstallment(Long paymentId, InstallmentRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new BusinessRuleException(
                "Payment not found", org.springframework.http.HttpStatus.NOT_FOUND));

        PaymentInstallment installment = new PaymentInstallment();
        installment.setPayment(payment);
        installment.setAmount(request.getAmount());
        installment.setInstallmentDate(request.getInstallmentDate());
        installment.setTransactionReference(request.getTransactionReference());
        
        return installmentRepository.save(installment);
    }
}
