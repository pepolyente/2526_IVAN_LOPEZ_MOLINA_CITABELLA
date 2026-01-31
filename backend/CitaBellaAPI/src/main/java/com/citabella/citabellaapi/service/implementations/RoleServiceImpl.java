package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
