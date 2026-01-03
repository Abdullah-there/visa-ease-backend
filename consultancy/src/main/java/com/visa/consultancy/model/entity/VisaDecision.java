package com.visa.consultancy.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visa_decision")
@Data
@NoArgsConstructor
public class VisaDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decision_id")
    private Long decisionId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private VisaApplication application;

    @Column(name = "decision_date", nullable = false)
    private LocalDate decisionDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type", nullable = false)
    private DecisionType decisionType;

    @Column(name = "decision_notes")
    private String decisionNotes;

    @OneToOne(mappedBy = "decision", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Visa visa;

    @OneToOne(mappedBy = "decision", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefusalLetter refusalLetter;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum DecisionType {
        ISSUED, REFUSED
    }
}