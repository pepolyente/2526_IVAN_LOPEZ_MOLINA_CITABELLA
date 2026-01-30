package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByNombre(String nombre);
}
