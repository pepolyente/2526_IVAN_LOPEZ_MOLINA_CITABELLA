package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.entity.security.Role;

public interface RoleService {
    Role getByName(String name);
}
