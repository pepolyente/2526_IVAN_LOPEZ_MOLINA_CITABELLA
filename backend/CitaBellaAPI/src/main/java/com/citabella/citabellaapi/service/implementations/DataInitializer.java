package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.security.Role;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.repository.RoleRepository;
import com.citabella.citabellaapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Transactional
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
        createRoleIfNotExist("USER", "User without anything assigned");
        createRoleIfNotExist("CLIENT", "Verified client");

        createUserIfNotExist("CitaBella", "admin@citabella.com", "citabella123", "ADMIN");
        createUserIfNotExist("Desconocido", "desconocido@opa.com", "hola123", "USER");
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }

    private void createUserIfNotExist(String name, String email, String password, String roleName) {
        if (userRepository.findByUsername(name).isEmpty()) {
            User user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            Role role = roleRepository.findByName(roleName).orElseThrow(()
                    -> new ResourceNotFoundException("Role not found"));
            user.setRole(role);
            userRepository.save(user);
        }
    }
}
