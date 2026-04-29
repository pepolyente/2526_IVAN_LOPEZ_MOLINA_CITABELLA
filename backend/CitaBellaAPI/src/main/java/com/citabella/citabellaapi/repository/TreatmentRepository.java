package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {

    boolean existsTreatmentByName(String name);

    List<Treatment> findAllByActive(Boolean active);

    Page<Treatment> findAllByActive(Boolean active, Pageable pageable);
}
