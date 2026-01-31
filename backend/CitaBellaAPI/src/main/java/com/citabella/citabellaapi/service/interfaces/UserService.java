package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse getById(Integer id);

    UserResponse getByEmail(String email);

    UserResponse getAuthenticated();

    boolean hasClient(Integer userId);

    void swapRole(Integer userId, String roleName);
}
