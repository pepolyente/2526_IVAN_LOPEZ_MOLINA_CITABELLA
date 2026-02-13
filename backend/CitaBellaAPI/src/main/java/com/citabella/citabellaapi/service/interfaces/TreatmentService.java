package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.treatment.Treatment;

import java.util.List;

public interface TreatmentService {
    TreatmentResponse create(TreatmentRequest request);

    TreatmentResponse getById(Integer id);

    List<TreatmentResponse> findAllActive();
}
