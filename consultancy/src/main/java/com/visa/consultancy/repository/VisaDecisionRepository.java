package com.visa.consultancy.repository;



import com.visa.consultancy.model.entity.VisaDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaDecisionRepository extends JpaRepository<VisaDecision, Long> {
}
