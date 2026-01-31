package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.TreatmentRepository;
import com.citabella.citabellaapi.service.interfaces.TreatmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public Treatment create(Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment getById(Integer id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found"));
    }

    @Override
    public List<Treatment> findAllActive() {
        return treatmentRepository.findAll()
                .stream().filter(Treatment::getActive).toList();
    }
}
