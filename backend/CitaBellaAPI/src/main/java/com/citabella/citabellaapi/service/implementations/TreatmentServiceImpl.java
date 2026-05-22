package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentDetailedResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.mappers.TreatmentMapper;
import com.citabella.citabellaapi.repository.TreatmentRepository;
import com.citabella.citabellaapi.repository.specifications.TreatmentSpecification;
import com.citabella.citabellaapi.service.interfaces.TreatmentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;


    @Override
    public TreatmentResponse create(TreatmentRequest request) {
        if (treatmentRepository.existsTreatmentByName(request.name())) {
            throw new BadRequestException("Treatment name already exists");
        }
        Treatment treatment = new Treatment();
        treatment.setName(request.name());
        treatment.setDescription(request.description());
        treatment.setMinimumDuration(request.minimumDuration());
        if (request.maximumDuration() != null) {
            treatment.setMaximumDuration(request.maximumDuration());
        }
        treatment.setPrice(request.price());
        return TreatmentMapper.toResponse(treatmentRepository.save(treatment));
    }

    @Override
    public TreatmentResponse getById(Integer id) {
        return TreatmentMapper.toResponse(
                treatmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Treatment not found")));
    }

    /**
     * Listado público activo (sin búsqueda dinámica, sin auth).
     * No requiere FilterRequest.
     */
    @Override
    public Page<TreatmentResponse> findAll(Pageable pageable, Boolean active) {
        if (active != null) {
            return treatmentRepository.findAllByActive(active, pageable)
                    .map(TreatmentMapper::toResponse);
        }
        return treatmentRepository.findAll(pageable)
                .map(TreatmentMapper::toResponse);
    }

    /**
     * Listado detallado para admin con filtros dinámicos.
     * Combina búsqueda por nombre (LIKE) y filtro por active mediante Specifications.
     */
    @Override
    public Page<TreatmentDetailedResponse> findAllDetailed(Pageable pageable, Boolean active, FilterRequest filterRequest) {
        String search = (filterRequest != null) ? filterRequest.search() : null;

        Specification<Treatment> spec = TreatmentSpecification.withFilters(search, active);

        return treatmentRepository.findAll(spec, pageable)
                .map(TreatmentMapper::toDetailedResponse);
    }

    @Override
    public List<TreatmentResponse> findAllActive() {
        return treatmentRepository.findAllByActive(true).stream()
                .map(TreatmentMapper::toResponse).toList();
    }

    @Override
    public TreatmentResponse update(Integer id, TreatmentRequest request) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        if (request.name() != null && !request.name().isBlank()) {
            if (!request.name().equals(treatment.getName())
                    && treatmentRepository.existsTreatmentByName(request.name())) {
                throw new BadRequestException("Treatment name already exists");
            }
            treatment.setName(request.name());
        }
        if (request.description() != null) treatment.setDescription(request.description());
        if (request.minimumDuration() != null) treatment.setMinimumDuration(request.minimumDuration());
        if (request.maximumDuration() != null) treatment.setMaximumDuration(request.maximumDuration());
        if (request.price() != null) treatment.setPrice(request.price());

        return TreatmentMapper.toResponse(treatmentRepository.save(treatment));
    }

    @Override
    public TreatmentResponse deactivate(Integer id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        if (!treatment.getActive()) {
            throw new BadRequestException("Treatment is already inactive");
        }
        treatment.setActive(false);
        return TreatmentMapper.toResponse(treatmentRepository.save(treatment));
    }

    @Override
    public TreatmentResponse activate(Integer id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));
        if (treatment.getActive()) {
            throw new BadRequestException("Treatment is already active");
        }
        treatment.setActive(true);
        return TreatmentMapper.toResponse(treatmentRepository.save(treatment));
    }
}
