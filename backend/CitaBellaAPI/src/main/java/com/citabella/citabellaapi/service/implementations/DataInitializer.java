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

        crearRolSiNoExiste("ADMIN", "Administrador del sistema");
        crearRolSiNoExiste("EMPLEADO", "Empleado del negocio");
        crearRolSiNoExiste("CLIENTE_PENDIENTE", "Usuario sin cliente asignado");
        crearRolSiNoExiste("CLIENTE", "Cliente verificado");
    }

    private void crearRolSiNoExiste(String nombre, String descripcion) {
        if (roleRepository.findByName(nombre).isEmpty()) {
            Role role = new Role();
            role.setName(nombre);
            role.setDescription(descripcion);
            roleRepository.save(role);
        }
    }
}
