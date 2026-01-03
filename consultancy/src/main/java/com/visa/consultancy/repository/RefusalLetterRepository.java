package com.visa.consultancy.repository;


import com.visa.consultancy.model.entity.RefusalLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefusalLetterRepository extends JpaRepository<RefusalLetter, Long> {
}