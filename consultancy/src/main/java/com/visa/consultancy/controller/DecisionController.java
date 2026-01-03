package com.visa.consultancy.controller;



import com.visa.consultancy.model.dto.DecisionRequest;
import com.visa.consultancy.model.entity.VisaDecision;
import com.visa.consultancy.service.DecisionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications/{id}/decision")
public class DecisionController {
    private final DecisionService decisionService;

    public DecisionController(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @PostMapping
    public ResponseEntity<VisaDecision> recordDecision(@PathVariable Long id, 
                                                      @Valid @RequestBody DecisionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(decisionService.recordDecision(id, request));
    }

    @GetMapping("/{decisionId}")
    public ResponseEntity<VisaDecision> getDecision(@PathVariable Long id, @PathVariable Long decisionId) {
        return ResponseEntity.ok(decisionService.getDecision(decisionId));
    }
}