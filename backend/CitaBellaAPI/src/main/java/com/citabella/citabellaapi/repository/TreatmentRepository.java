package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    boolean existsTreatmentByName(String name);
}
