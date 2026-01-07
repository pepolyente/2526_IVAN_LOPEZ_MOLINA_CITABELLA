package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.empleado.EmpleadoServicio;
import com.citabella.citabellaapi.entity.empleado.EmpleadoServicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoServicioRepository extends JpaRepository<EmpleadoServicio, EmpleadoServicioId> {

    boolean existsById_IdEmpleadoAndId_IdServicio(Integer idEmpleado, Integer idServicio);
    /*List<EmpleadoServicio> findByEmpleado_IdEmpleado(Integer idEmpleado);*/
}

