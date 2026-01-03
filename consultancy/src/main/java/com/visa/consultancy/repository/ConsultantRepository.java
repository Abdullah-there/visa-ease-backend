package com.visa.consultancy.repository;


import com.visa.consultancy.model.entity.VisaConsultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepository extends JpaRepository<VisaConsultant, Long> {
}
