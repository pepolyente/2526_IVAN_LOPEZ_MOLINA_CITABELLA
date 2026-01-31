package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        //TODO revise logic
        createRoleIfNotExist("ADMIN", "System administrator");
        createRoleIfNotExist("EMPLOYEE", "Employee of the company");
        createRoleIfNotExist("PENDING_CLIENT", "User without client assigned");
        createRoleIfNotExist("CLIENT", "Verified client");
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}
