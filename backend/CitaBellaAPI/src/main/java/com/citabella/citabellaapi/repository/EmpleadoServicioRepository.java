package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.employee.EmpleadoServicio;
import com.citabella.citabellaapi.entity.employee.EmpleadoServicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoServicioRepository extends JpaRepository<EmpleadoServicio, EmpleadoServicioId> {

    boolean existsById_IdEmpleadoAndId_IdServicio(Integer idEmpleado, Integer idServicio);
    /*List<EmpleadoServicio> findByEmpleado_IdEmpleado(Integer idEmpleado);*/
}

