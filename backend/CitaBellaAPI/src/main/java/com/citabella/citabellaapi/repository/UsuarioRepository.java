package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);

    // Pa después
    //List<Usuario> findByRolNombreAndClienteIsNull(String rolNombre);

}
