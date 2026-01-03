package com.visa.consultancy.service;

import com.visa.consultancy.model.dto.ApplicationRequest;
import com.visa.consultancy.model.entity.*;
import com.visa.consultancy.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.visa.consultancy.exception.BusinessRuleException;
import java.util.Optional;

@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;
    private final ConsultantRepository consultantRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
            ApplicantRepository applicantRepository,
            ConsultantRepository consultantRepository) {
        this.applicationRepository = applicationRepository;
        this.applicantRepository = applicantRepository;
        this.consultantRepository = consultantRepository;
    }

    public VisaApplication createApplication(ApplicationRequest request) {
        System.out.println(request.getApplicantId());
        System.out.println(request.getConsultantId());
        VisaApplicant applicant = applicantRepository.findById(request.getApplicantId())
                .orElseThrow(() -> new BusinessRuleException(
                        "Applicant not found", org.springframework.http.HttpStatus.NOT_FOUND));

        VisaConsultant consultant = null;
        if (request.getConsultantId() != null) {
            consultant = consultantRepository.findById(request.getConsultantId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "Consultant not found", org.springframework.http.HttpStatus.NOT_FOUND));
        }

        VisaApplication application = new VisaApplication();
        application.setApplicant(applicant);
        application.setConsultant(consultant);
        application.setVisaType(request.getVisaType());

        return applicationRepository.save(application);
    }

    public VisaApplication getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessRuleException(
                        "Application not found", org.springframework.http.HttpStatus.NOT_FOUND));
    }

    public java.util.List<VisaApplication> getApplicationsByApplicant(Long applicantId) {
        return applicationRepository.findByApplicant_ApplicantId(applicantId);
    }

    public java.util.List<VisaApplication> getApplicationsByConsultant(Long consultantId) {
        return applicationRepository.findByConsultant_ConsultantId(consultantId);
    }

    public VisaApplication updateApplication(Long applicationId, ApplicationRequest request) {
        VisaApplication application = getApplication(applicationId);

        if (request.getConsultantId() != null) {
            VisaConsultant consultant = consultantRepository.findById(request.getConsultantId())
                    .orElseThrow(() -> new BusinessRuleException(
                            "Consultant not found", org.springframework.http.HttpStatus.NOT_FOUND));
            application.setConsultant(consultant);
        }

        if (request.getVisaType() != null)
            application.setVisaType(request.getVisaType());

        return applicationRepository.save(application);
    }

    public VisaApplication submitApplication(Long applicationId) {
        VisaApplication application = getApplication(applicationId);

        // Validate that application is in DRAFT status before submission
        if (!application.getStatus().equals(VisaApplication.ApplicationStatus.DRAFT)) {
            throw new BusinessRuleException(
                    "Application can only be submitted from DRAFT status",
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        // Validate that documents are ready
        if (!application.getIsDocumentFileReady()) {
            throw new BusinessRuleException(
                    "All required documents must be uploaded before submission",
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        // Update status to SUBMITTED
        application.setStatus(VisaApplication.ApplicationStatus.SUBMITTED);
        return applicationRepository.save(application);
    }
}
