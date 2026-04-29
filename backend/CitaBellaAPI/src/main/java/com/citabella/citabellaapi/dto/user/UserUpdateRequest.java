package com.citabella.citabellaapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 4, max = 100)
        String username,

        @Email
        String email,

        @Size(min = 6)
        String password
) {
}
