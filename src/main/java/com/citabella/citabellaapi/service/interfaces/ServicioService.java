package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.servicio.Servicio;

import java.util.List;

public interface ServicioService {
    Servicio crear(Servicio servicio);
    Servicio obtener(Integer id);
    List<Servicio> listarActivos();
}
