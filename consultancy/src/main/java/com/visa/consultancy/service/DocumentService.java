package com.visa.consultancy.service;



import com.visa.consultancy.model.dto.DocumentRequest;
import com.visa.consultancy.model.entity.*;
import com.visa.consultancy.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.visa.consultancy.exception.BusinessRuleException;

@Service
@Transactional
public class DocumentService {
    private final DocumentLineItemRepository lineItemRepository;
    private final DocumentRepository documentRepository;
    private final ApplicationRepository applicationRepository;

    public DocumentService(DocumentLineItemRepository lineItemRepository,
                           DocumentRepository documentRepository,
                           ApplicationRepository applicationRepository) {
        this.lineItemRepository = lineItemRepository;
        this.documentRepository = documentRepository;
        this.applicationRepository = applicationRepository;
    }

    public DocumentLineItem addDocumentToApplication(Long applicationId, DocumentRequest request) {
        VisaApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new BusinessRuleException(
                "Application not found", org.springframework.http.HttpStatus.NOT_FOUND));

        Document document = documentRepository.findById(request.getDocumentId())
            .orElseThrow(() -> new BusinessRuleException(
                "Document template not found", org.springframework.http.HttpStatus.NOT_FOUND));

        DocumentLineItem lineItem = new DocumentLineItem();
        lineItem.setApplication(application);
        lineItem.setDocument(document);
        lineItem.setFilePath(request.getFilePath());
        lineItem.setNotes(request.getNotes());
        lineItem.setUploadDate(java.time.LocalDateTime.now());
        
        return lineItemRepository.save(lineItem);
    }

    public java.util.List<DocumentLineItem> getDocumentsByApplication(Long applicationId) {
        return lineItemRepository.findAll().stream()
            .filter(d -> d.getApplication() != null && d.getApplication().getApplicationId().equals(applicationId))
            .toList();
    }

    public DocumentLineItem getDocumentLineItem(Long id) {
        return lineItemRepository.findById(id)
            .orElseThrow(() -> new com.visa.consultancy.exception.BusinessRuleException(
                "Document line item not found", org.springframework.http.HttpStatus.NOT_FOUND));
    }
}
