package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.treatment.Treatment;

import java.util.Set;
import java.util.stream.Collectors;

public class TreatmentMapper {

    public static TreatmentResponse toResponse(Treatment treatment) {
        if (treatment == null) return null;

        return new TreatmentResponse(
                treatment.getId(),
                treatment.getName(),
                treatment.getMinimumDuration(),
                treatment.getPrice(),
                treatment.getActive()
        );
    }

    public static Set<TreatmentResponse> toResponseSet(Set<Treatment> treatments) {
        if (treatments == null) return Set.of();

        return treatments.stream()
                .map(TreatmentMapper::toResponse)
                .collect(Collectors.toSet());
    }
}