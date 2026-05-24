package com.citabella.citabellaapi.dto.user;

import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.enums.ProfileType;

public record UserResponse(
        Integer id,
        String username,
        String email,
        String role,
        AccountStatus accountStatus,
        ProfileType profileType
) {
}
