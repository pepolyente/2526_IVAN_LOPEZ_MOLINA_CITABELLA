package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.user.UsuarioRequest;
import com.citabella.citabellaapi.dto.user.UsuarioResponse;

public interface UserService {

    UsuarioResponse create(UsuarioRequest request);

    UsuarioResponse getById(Integer id);

    UsuarioResponse getByEmail(String email);

    UsuarioResponse getAuthenticated();

    boolean hasClient(Integer userId);

    void swapRole(Integer userId, String roleName);
}
