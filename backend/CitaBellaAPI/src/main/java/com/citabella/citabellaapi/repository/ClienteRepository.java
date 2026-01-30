package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Client, Integer> {

    boolean existsByTelefono(String telefono);

    boolean existsByUsuario_IdUsuario(Integer idUsuario);
}
