package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.employee.EmployeeTreatment;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoServicioRepository extends JpaRepository<EmployeeTreatment, EmployeeTreatmentId> {

    boolean existsById_IdEmpleadoAndId_IdServicio(Integer idEmpleado, Integer idServicio);
    /*List<EmpleadoServicio> findByEmpleado_IdEmpleado(Integer idEmpleado);*/
}

