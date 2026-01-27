package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.entity.security.Rol;
import com.citabella.citabellaapi.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void init() {

        crearRolSiNoExiste("ADMIN", "Administrador del sistema");
        crearRolSiNoExiste("EMPLEADO", "Empleado del negocio");
        crearRolSiNoExiste("CLIENTE_PENDIENTE", "Usuario sin cliente asignado");
        crearRolSiNoExiste("CLIENTE", "Cliente verificado");
    }

    private void crearRolSiNoExiste(String nombre, String descripcion) {
        if (rolRepository.findByNombre(nombre).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            rolRepository.save(rol);
        }
    }
}
