package com.visa.consultancy.service;



import com.visa.consultancy.model.dto.ApplicantRequest;
import com.visa.consultancy.model.entity.VisaApplicant;
import com.visa.consultancy.repository.ApplicantRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    public VisaApplicant createApplicant(ApplicantRequest request) {
        validatePassportExpiry(request.getPassportExpiryDate());
        
        VisaApplicant applicant = new VisaApplicant();
        applicant.setFirstName(request.getFirstName());
        applicant.setLastName(request.getLastName());
        applicant.setDateOfBirth(request.getDateOfBirth());
        applicant.setPassportNumber(request.getPassportNumber());
        applicant.setPassportExpiryDate(request.getPassportExpiryDate());
        applicant.setEmail(request.getEmail());
        applicant.setPassword(request.getPassword());
        applicant.setPhone(request.getPhone());
        applicant.setNationality(request.getNationality());
        
        return applicantRepository.save(applicant);
    }

    public VisaApplicant getApplicant(Long applicantId) {
        return applicantRepository.findById(applicantId)
            .orElseThrow(() -> new com.visa.consultancy.exception.BusinessRuleException(
                "Applicant not found", 
                org.springframework.http.HttpStatus.NOT_FOUND
            ));
    }

    public VisaApplicant updateApplicant(Long applicantId, ApplicantRequest request) {
        VisaApplicant applicant = getApplicant(applicantId);

        // validate passport expiry if provided
        if (request.getPassportExpiryDate() != null) {
            validatePassportExpiry(request.getPassportExpiryDate());
            applicant.setPassportExpiryDate(request.getPassportExpiryDate());
        }

        if (request.getFirstName() != null) applicant.setFirstName(request.getFirstName());
        if (request.getLastName() != null) applicant.setLastName(request.getLastName());
        if (request.getDateOfBirth() != null) applicant.setDateOfBirth(request.getDateOfBirth());
        if (request.getPassportNumber() != null) applicant.setPassportNumber(request.getPassportNumber());
        if (request.getEmail() != null) applicant.setEmail(request.getEmail());
        if (request.getPhone() != null) applicant.setPhone(request.getPhone());
        if (request.getNationality() != null) applicant.setNationality(request.getNationality());

        return applicantRepository.save(applicant);
    }

    private void validatePassportExpiry(LocalDate expiryDate) {
        if (expiryDate.isBefore(LocalDate.now().plusMonths(6))) {
            throw new com.visa.consultancy.exception.BusinessRuleException(
                "Passport must be valid for at least 6 months", 
                org.springframework.http.HttpStatus.BAD_REQUEST
            );
        }
    }
}
