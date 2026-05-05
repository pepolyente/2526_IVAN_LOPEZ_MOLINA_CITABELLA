package com.citabella.citabellaapi.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}
