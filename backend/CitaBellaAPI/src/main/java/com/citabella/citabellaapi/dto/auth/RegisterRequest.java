package com.citabella.citabellaapi.dto.auth;

public record RegisterRequest(
        String username,
        String password,
        String email
) {
}
