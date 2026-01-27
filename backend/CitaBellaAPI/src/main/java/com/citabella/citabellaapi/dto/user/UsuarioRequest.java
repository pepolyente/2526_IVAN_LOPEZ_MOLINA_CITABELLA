package com.citabella.citabellaapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank
        @Size(min = 4,max = 100)
        String nombreUsuario,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 6)
        String password
) {}
