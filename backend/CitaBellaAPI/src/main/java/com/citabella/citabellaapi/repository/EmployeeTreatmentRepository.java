package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.employee.EmployeeTreatment;
import com.citabella.citabellaapi.entity.employee.EmployeeTreatmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTreatmentRepository extends JpaRepository<EmployeeTreatment, EmployeeTreatmentId> {

    boolean existsById_employeeIdAndId_treatmentId(Integer employeeId, Integer treatmentId);

}

