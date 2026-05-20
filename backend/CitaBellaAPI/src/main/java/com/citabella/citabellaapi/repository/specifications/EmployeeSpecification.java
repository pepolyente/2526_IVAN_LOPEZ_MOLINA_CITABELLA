package com.citabella.citabellaapi.repository.specifications;

import com.citabella.citabellaapi.entity.employee.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    /**
     * Búsqueda LIKE por nombre de empleado (case-insensitive).
     */
    public static Specification<Employee> byName(String search) {
        return BaseSpecifications.byFieldLike(search, "name");
    }

    /**
     * Filtro por estado activo/inactivo.
     */
    public static Specification<Employee> byActive(Boolean active) {
        return BaseSpecifications.byActive(active);
    }

    /**
     * Combina filtro por nombre y por active.
     */
    public static Specification<Employee> withFilters(String search, Boolean active) {

        List<Specification<Employee>> specifications = new ArrayList<>();

        Specification<Employee> nameSpec = byName(search);
        if (nameSpec != null) {
            specifications.add(nameSpec);
        }

        Specification<Employee> activeSpec = byActive(active);
        if (activeSpec != null) {
            specifications.add(activeSpec);
        }

        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}