package com.visa.consultancy.controller;



import com.visa.consultancy.model.dto.ApplicantRequest;
import com.visa.consultancy.model.entity.VisaApplicant;
import com.visa.consultancy.service.ApplicantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applicants")
@CrossOrigin(origins = "*")
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping
    public ResponseEntity<VisaApplicant> createApplicant(@Valid @RequestBody ApplicantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicantService.createApplicant(request));
    }

    @GetMapping("/{applicantId}")
    public ResponseEntity<VisaApplicant> getApplicant(@PathVariable Long applicantId) {
        return ResponseEntity.ok(applicantService.getApplicant(applicantId));
    }

    @PutMapping("/{applicantId}")
    public ResponseEntity<VisaApplicant> updateApplicant(@PathVariable Long applicantId,
                                                         @Valid @RequestBody ApplicantRequest request) {
        return ResponseEntity.ok(applicantService.updateApplicant(applicantId, request));
    }
}
