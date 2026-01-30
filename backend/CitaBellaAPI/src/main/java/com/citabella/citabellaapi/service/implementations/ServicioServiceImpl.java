package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.TreatmentRepository;
import com.citabella.citabellaapi.service.interfaces.ServicioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final TreatmentRepository treatmentRepository;

    public ServicioServiceImpl(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public Treatment crear(Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment obtener(Integer id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    @Override
    public List<Treatment> listarActivos() {
        return treatmentRepository.findAll()
                .stream().filter(Treatment::getActive).toList();
    }
}
