package com.visa.consultancy.service;



import com.visa.consultancy.model.dto.ConsultantRequest;
import com.visa.consultancy.model.entity.VisaApplicant;
import com.visa.consultancy.model.entity.VisaConsultant;
import com.visa.consultancy.repository.ConsultantRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class ConsultantService {
    private final ConsultantRepository consultantRepository;

    public ConsultantService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public VisaConsultant createApplicant(ConsultantRequest request) {
    
        VisaConsultant applicant = new VisaConsultant();
        applicant.setFirstName(request.getFirstName());
        applicant.setLastName(request.getLastName());
        applicant.setEmail(request.getEmail());
        applicant.setPassword(request.getPassword());
        applicant.setPhone(request.getPhone());
        applicant.setSpecialization(request.getSpecialization());

        return consultantRepository.save(applicant);
    }

    public VisaConsultant getApplicant(Long consultantId) {
        return consultantRepository.findById(consultantId)
            .orElseThrow(() -> new com.visa.consultancy.exception.BusinessRuleException(
                "Applicant not found", 
                org.springframework.http.HttpStatus.NOT_FOUND
            ));
    }

    // public VisaApplicant updateApplicant(Long consultantId, ConsultantRequest request) {
    //     VisaApplicant applicant = getApplicant(consultantId);

    //     if (request.getFirstName() != null) applicant.setFirstName(request.getFirstName());
    //     if (request.getLastName() != null) applicant.setLastName(request.getLastName());
    //     if (request.getEmail() != null) applicant.setEmail(request.getEmail());
    //     if (request.getPhone() != null) applicant.setPhone(request.getPhone());

    //     return consultantRepository.save(applicant);
    // }
}
