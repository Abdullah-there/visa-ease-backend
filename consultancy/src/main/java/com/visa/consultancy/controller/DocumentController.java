package com.visa.consultancy.controller;


import com.visa.consultancy.model.dto.DocumentRequest;
import com.visa.consultancy.model.entity.DocumentLineItem;
import com.visa.consultancy.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/applications/{id}/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<DocumentLineItem> addDocument(@PathVariable Long id, 
                                                       @Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
            .body(documentService.addDocumentToApplication(id, request));
    }

    @GetMapping
    public ResponseEntity<java.util.List<com.visa.consultancy.model.entity.DocumentLineItem>> getDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentsByApplication(id));
    }

    @GetMapping("/{lineItemId}")
    public ResponseEntity<com.visa.consultancy.model.entity.DocumentLineItem> getDocument(@PathVariable Long lineItemId) {
        return ResponseEntity.ok(documentService.getDocumentLineItem(lineItemId));
    }
}

