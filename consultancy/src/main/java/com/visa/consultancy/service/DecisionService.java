package com.visa.consultancy.service;


import com.visa.consultancy.model.dto.DecisionRequest;
import com.visa.consultancy.model.entity.*;
import com.visa.consultancy.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.visa.consultancy.exception.BusinessRuleException;

@Service
@Transactional
public class DecisionService {
    private final VisaDecisionRepository decisionRepository;
    private final VisaRepository visaRepository;
    private final RefusalLetterRepository refusalLetterRepository;
    private final ApplicationRepository applicationRepository;

    public DecisionService(VisaDecisionRepository decisionRepository,
                           VisaRepository visaRepository,
                           RefusalLetterRepository refusalLetterRepository,
                           ApplicationRepository applicationRepository) {
        this.decisionRepository = decisionRepository;
        this.visaRepository = visaRepository;
        this.refusalLetterRepository = refusalLetterRepository;
        this.applicationRepository = applicationRepository;
    }

    public VisaDecision recordDecision(Long applicationId, DecisionRequest request) {
        VisaApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new BusinessRuleException(
                "Application not found", org.springframework.http.HttpStatus.NOT_FOUND));

        // Check if decision already exists
        if (application.getDecision() != null) {
            throw new com.visa.consultancy.exception.BusinessRuleException(
                "Decision already exists for this application", 
                org.springframework.http.HttpStatus.CONFLICT);
        }

        VisaDecision decision = new VisaDecision();
        decision.setApplication(application);
        decision.setDecisionType(VisaDecision.DecisionType.valueOf(request.getDecisionType()));
        decision.setDecisionNotes(request.getDecisionNotes());
        
        VisaDecision savedDecision = decisionRepository.save(decision);

        if ("ISSUED".equals(request.getDecisionType())) {
            createVisa(savedDecision, request.getVisaNumber());
        } else if ("REFUSED".equals(request.getDecisionType())) {
            createRefusalLetter(savedDecision, request.getRefusalReason());
        }

        return savedDecision;
    }

    public VisaDecision getDecision(Long decisionId) {
        return decisionRepository.findById(decisionId)
            .orElseThrow(() -> new com.visa.consultancy.exception.BusinessRuleException(
                "Decision not found", org.springframework.http.HttpStatus.NOT_FOUND));
    }

    private void createVisa(VisaDecision decision, String visaNumber) {
        Visa visa = new Visa();
        visa.setDecision(decision);
        visa.setVisaNumber(visaNumber);
        visa.setIssueDate(java.time.LocalDate.now());
        visa.setExpiryDate(java.time.LocalDate.now().plusYears(1));
        visaRepository.save(visa);
    }

    private void createRefusalLetter(VisaDecision decision, String reason) {
        RefusalLetter refusalLetter = new RefusalLetter();
        refusalLetter.setDecision(decision);
        refusalLetter.setRefusalReason(reason);
        refusalLetter.setAppealDeadline(java.time.LocalDate.now().plusDays(30));
        refusalLetterRepository.save(refusalLetter);
    }
}
