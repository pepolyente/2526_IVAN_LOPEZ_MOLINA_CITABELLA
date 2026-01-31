package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.treatment.Treatment;

import java.util.List;

public interface TreatmentService {
    Treatment create(Treatment treatment);

    Treatment getById(Integer id);

    List<Treatment> findAllActive();
}
