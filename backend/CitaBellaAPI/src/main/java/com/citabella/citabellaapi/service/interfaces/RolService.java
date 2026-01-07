package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.seguridad.Rol;

public interface RolService {
    Rol obtenerPorNombre(String nombre);
}
