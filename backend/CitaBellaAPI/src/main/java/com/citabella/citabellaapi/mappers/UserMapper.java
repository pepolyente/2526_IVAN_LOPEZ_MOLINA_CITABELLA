package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.entity.security.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName(),
                user.getAccountStatus(),
                user.getProfileType()
        );
    }
}
