package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse getById(Integer id);

    UserResponse getByEmail(String email);

    UserResponse getAuthenticated();

    boolean hasClient(Integer userId);

    UserInfoResponse swapRole(Integer userId, String roleName);

    List<UserResponse> getAll();

}
