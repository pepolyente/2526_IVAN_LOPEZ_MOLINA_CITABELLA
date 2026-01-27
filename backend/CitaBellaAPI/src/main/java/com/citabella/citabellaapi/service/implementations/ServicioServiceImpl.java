package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.treatment.Servicio;
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
    public Servicio crear(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public Servicio obtener(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    @Override
    public List<Servicio> listarActivos() {
        return servicioRepository.findAll()
                .stream().filter(Servicio::getActivo).toList();
    }
}
