package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.security.Rol;

public interface RolService {
    Rol obtenerPorNombre(String nombre);
}
