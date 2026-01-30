package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.repository.ServicioRepository;
import com.citabella.citabellaapi.service.interfaces.ServicioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public Treatment crear(Treatment treatment) {
        return servicioRepository.save(treatment);
    }

    @Override
    public Treatment obtener(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    @Override
    public List<Treatment> listarActivos() {
        return servicioRepository.findAll()
                .stream().filter(Treatment::getActive).toList();
    }
}
