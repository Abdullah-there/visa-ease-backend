package com.visa.consultancy.controller;


import com.visa.consultancy.model.dto.ConsultantRequest;
import com.visa.consultancy.model.entity.VisaConsultant;
import com.visa.consultancy.repository.ConsultantRepository;
import com.visa.consultancy.service.ApplicantService;
import com.visa.consultancy.service.ConsultantService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultant")
@CrossOrigin(origins = "*")
public class ConsultantController {
    private final ConsultantService consultantService;

    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @PostMapping
    public ResponseEntity<VisaConsultant> createApplicant(@Valid @RequestBody ConsultantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultantService.createApplicant(request));
    }

    // @GetMapping("/{applicantId}")
    // public ResponseEntity<VisaApplicant> getApplicant(@PathVariable Long applicantId) {
    //     return ResponseEntity.ok(consultantService.getApplicant(applicantId));
    // }

    // @PutMapping("/{applicantId}")
    // public ResponseEntity<VisaApplicant> updateApplicant(@PathVariable Long applicantId,
    //                                                      @Valid @RequestBody ApplicantRequest request) {
    //     return ResponseEntity.ok(consultantService.updateApplicant(applicantId, request));
    // }
}
