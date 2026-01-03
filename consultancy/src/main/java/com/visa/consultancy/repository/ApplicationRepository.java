package com.visa.consultancy.repository;



import com.visa.consultancy.model.entity.VisaApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<VisaApplication, Long> {
    List<VisaApplication> findByApplicant_ApplicantId(Long applicantId);
    List<VisaApplication> findByConsultant_ConsultantId(Long consultantId);
}

