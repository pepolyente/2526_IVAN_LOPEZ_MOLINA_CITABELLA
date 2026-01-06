package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.usuario.UsuarioRequest;
import com.citabella.citabellaapi.dto.usuario.UsuarioResponse;
import com.citabella.citabellaapi.entity.seguridad.Usuario;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioRequest request);

    UsuarioResponse obtenerPorId(Integer id);

    UsuarioResponse obtenerPorEmail(String email);

    UsuarioResponse obtenerUsuarioAutenticado();

    boolean tieneCliente(Integer idUsuario);

    void cambiarRol(Integer idUsuario, String nombreRol);
}
