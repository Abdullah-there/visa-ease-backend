package com.visa.consultancy.repository;



import com.visa.consultancy.model.entity.VisaApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<VisaApplicant, Long> {
}