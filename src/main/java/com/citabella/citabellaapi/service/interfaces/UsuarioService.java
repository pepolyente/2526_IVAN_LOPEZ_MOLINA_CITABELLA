package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.seguridad.Usuario;

public interface UsuarioService {

    Usuario crearUsuario(String nombreUsuario, String email, String password);

    Usuario obtenerPorId(Integer id);

    Usuario obtenerPorEmail(String email);

    Usuario obtenerUsuarioAutenticado();

    boolean tieneCliente(Integer idUsuario);

    void cambiarRol(Integer idUsuario, String nombreRol);
}
