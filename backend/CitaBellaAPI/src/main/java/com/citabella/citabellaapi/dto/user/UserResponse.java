package com.citabella.citabellaapi.dto.user;

public record UserResponse(
        Integer id,
        String username,
        String email,
        String role
        //TIPO PERFIL ??
        //OPTIONAL PERFILID
) {
}
