package com.citabella.citabellaapi.dto.usuario;

public record UsuarioResponse(
        Integer idUsuario,
        String nombreUsuario,
        String email,
        String rol
        //TIPO PERFIL ??
        //OPTIONAL PERFILID
) {
}
