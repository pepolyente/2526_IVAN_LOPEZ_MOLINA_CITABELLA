package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.repository.TreatmentRepository;
import com.citabella.citabellaapi.service.interfaces.TreatmentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;


    @Override
    public TreatmentResponse create(TreatmentRequest request) {
        Treatment treatment = new Treatment();
        if (treatmentRepository.existsTreatmentByName(request.name())){
            throw new BadRequestException("Treatment's name already exists");
        }
        treatment.setName(request.name());
        treatment.setDescription(request.name());
        treatment.setMinimumDuration(request.minimumDuration());
        if (request.maximumDuration() != null){
            treatment.setMaximumDuration(request.maximumDuration());
        }
        treatment.setPrice(request.price());
        Treatment createdTreatment = treatmentRepository.save(treatment);
        return mapToResponse(createdTreatment);
    }

    @Override
    public TreatmentResponse getById(Integer id) {
        Treatment treatment = treatmentRepository.findById(id) .orElseThrow(()
                -> new ResourceNotFoundException("Treatment not found"));

        return mapToResponse(treatment);
    }

    @Override
    public List<TreatmentResponse> findAllActive() {

        List<TreatmentResponse> response = new ArrayList<>();
                List<Treatment> treatments = treatmentRepository.findAll()
                .stream().filter(Treatment::getActive).toList();
                if ( treatments.isEmpty()){
                    throw new ResourceNotFoundException("Treatments not found");
                }
        for (Treatment treatment : treatments){
            response.add(mapToResponse(treatment));
        }

        return response;
    }

    private TreatmentResponse mapToResponse(Treatment treatment) {
        return new TreatmentResponse(
                treatment.getId(),
                treatment.getName(),
                treatment.getMinimumDuration(),
                treatment.getPrice(),
                treatment.getActive()
        );
    }
}
