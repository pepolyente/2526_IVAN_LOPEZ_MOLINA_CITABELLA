package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentDetailedResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TreatmentService {

    TreatmentResponse create(TreatmentRequest request);

    TreatmentResponse getById(Integer id);

    Page<TreatmentResponse> findAll(Pageable pageable, Boolean active);

    Page<TreatmentDetailedResponse> findAllDetailed(Pageable pageable, Boolean active, FilterRequest filterRequest);

    TreatmentResponse update(Integer id, TreatmentRequest request);

    TreatmentResponse deactivate(Integer id);

    TreatmentResponse activate(Integer id);

    List<TreatmentResponse> findAllActive();
}
