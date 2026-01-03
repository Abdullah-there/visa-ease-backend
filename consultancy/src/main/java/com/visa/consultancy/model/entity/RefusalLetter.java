package com.visa.consultancy.model.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "refusal_letter")
@Data
@NoArgsConstructor
public class RefusalLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refusal_id")
    private Long refusalId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "decision_id", nullable = false, unique = true)
    private VisaDecision decision;

    @Column(name = "refusal_date", nullable = false)
    private LocalDate refusalDate = LocalDate.now();

    @Column(name = "refusal_reason", nullable = false, columnDefinition = "TEXT")
    private String refusalReason;

    @Column(name = "appeal_deadline")
    private LocalDate appealDeadline;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
