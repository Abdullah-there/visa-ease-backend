package com.visa.consultancy.controller;



import com.visa.consultancy.model.dto.ApplicationRequest;
import com.visa.consultancy.model.entity.VisaApplication;
import com.visa.consultancy.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<VisaApplication> createApplication(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(request));
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<VisaApplication> updateApplication(@PathVariable Long applicationId,
                                                             @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.updateApplication(applicationId, request));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<VisaApplication> getApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.getApplication(applicationId));
    }

    @GetMapping("/applicant/{id}")
    public ResponseEntity<List<VisaApplication>> getApplicationsByApplicant(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationsByApplicant(id));
    }

    @GetMapping("/consultant/{id}")
    public ResponseEntity<List<VisaApplication>> getApplicationsByConsultant(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationsByConsultant(id));
    }

    @PostMapping("/{applicationId}/submit")
    public ResponseEntity<VisaApplication> submitApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.submitApplication(applicationId));
    }
}
