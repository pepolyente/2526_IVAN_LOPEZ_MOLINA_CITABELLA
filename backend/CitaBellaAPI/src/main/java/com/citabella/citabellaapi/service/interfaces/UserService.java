package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.auth.UserInfoResponse;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.user.UserRequest;
import com.citabella.citabellaapi.dto.user.UserResponse;
import com.citabella.citabellaapi.dto.user.UserUpdateRequest;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse getById(Integer id);

    UserResponse getByEmail(String email);

    UserResponse getAuthenticated();

    UserResponse update(Integer id, UserUpdateRequest request);

    UserResponse deactivate(Integer id);   // accountStatus → LOCKED

    boolean hasClient(Integer userId);

    UserInfoResponse swapRole(Integer userId, String roleName);

    Page<UserResponse> findAll(Pageable pageable, AccountStatus accountStatus, FilterRequest filterRequest);
}
