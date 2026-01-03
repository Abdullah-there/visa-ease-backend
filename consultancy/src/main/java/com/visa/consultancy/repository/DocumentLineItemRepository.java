package com.visa.consultancy.repository;



import com.visa.consultancy.model.entity.DocumentLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLineItemRepository extends JpaRepository<DocumentLineItem, Long> {
}
