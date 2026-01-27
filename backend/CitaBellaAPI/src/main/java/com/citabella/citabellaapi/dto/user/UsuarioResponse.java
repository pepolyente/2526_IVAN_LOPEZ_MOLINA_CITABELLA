package com.citabella.citabellaapi.dto.user;

public record UsuarioResponse(
        Integer idUsuario,
        String nombreUsuario,
        String email,
        String rol
        //TIPO PERFIL ??
        //OPTIONAL PERFILID
) {
}
