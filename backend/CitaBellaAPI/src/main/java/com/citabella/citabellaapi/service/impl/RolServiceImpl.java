package com.citabella.citabellaapi.service.impl;

import com.citabella.citabellaapi.entity.seguridad.Rol;
import com.citabella.citabellaapi.repository.RolRepository;
import com.citabella.citabellaapi.service.interfaces.RolService;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol obtenerPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
}
