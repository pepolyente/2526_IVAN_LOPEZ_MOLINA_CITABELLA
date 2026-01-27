package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.user.UsuarioRequest;
import com.citabella.citabellaapi.dto.user.UsuarioResponse;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioRequest request);

    UsuarioResponse obtenerPorId(Integer id);

    UsuarioResponse obtenerPorEmail(String email);

    UsuarioResponse obtenerUsuarioAutenticado();

    boolean tieneCliente(Integer idUsuario);

    void cambiarRol(Integer idUsuario, String nombreRol);
}
