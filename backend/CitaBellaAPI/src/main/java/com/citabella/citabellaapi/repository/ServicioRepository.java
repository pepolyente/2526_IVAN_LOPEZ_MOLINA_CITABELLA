package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.treatment.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio,Integer> {

}
