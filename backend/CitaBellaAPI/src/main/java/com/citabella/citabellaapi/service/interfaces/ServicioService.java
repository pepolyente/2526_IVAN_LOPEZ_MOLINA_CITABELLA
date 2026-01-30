package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.treatment.Treatment;

import java.util.List;

public interface ServicioService {
    Treatment crear(Treatment treatment);

    Treatment obtener(Integer id);

    List<Treatment> listarActivos();
}
