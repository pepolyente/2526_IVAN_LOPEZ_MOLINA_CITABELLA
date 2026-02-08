package com.citabella.citabellaapi.dto.auth;

public record UserInfoResponse(
        Integer id,
        String username,
        String email,
        String role
) {
}
