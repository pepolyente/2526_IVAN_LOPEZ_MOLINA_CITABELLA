package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        //TODO revise logic
        createRoleIfNotExist("ADMIN", "System administrator");
        createRoleIfNotExist("EMPLOYEE", "Employee of the company");
        createRoleIfNotExist("NONE", "User without anything assigned");
        createRoleIfNotExist("CLIENT", "Verified client");
        createUserIfNotExist("CitaBella", "admin@citabella.com", "citabella123");
        createUserIfNotExist("Desconocido", "desconocido@opa.com", "hola123");
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }

    private void createUserIfNotExist(String name, String email, String password) {
        if (userRepository.findByUsername(name).isEmpty()) {
            User user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }
}
