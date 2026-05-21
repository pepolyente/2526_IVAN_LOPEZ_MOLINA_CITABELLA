package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.employee.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
    boolean existsByName(String name);

    List<Employee> findAllByActive(Boolean active);

    Page<Employee> findAllByActive(Boolean active, Pageable pageable);
}
