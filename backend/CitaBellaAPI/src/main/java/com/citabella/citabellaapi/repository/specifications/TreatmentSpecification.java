package com.citabella.citabellaapi.repository.specifications;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TreatmentSpecification {

    private TreatmentSpecification() {
    }

    /**
     * Búsqueda LIKE por nombre de tratamiento (case-insensitive).
     */
    public static Specification<Treatment> byName(String search) {
        return BaseSpecifications.byFieldLike(search, "name");
    }

    /**
     * Filtro por estado activo/inactivo.
     */
    public static Specification<Treatment> byActive(Boolean active) {
        return BaseSpecifications.byActive(active);
    }

    /**
     * Combina filtro por nombre y por active.
     */
    public static Specification<Treatment> withFilters(String search, Boolean active) {

        List<Specification<Treatment>> specifications = new ArrayList<>();

        Specification<Treatment> nameSpec = byName(search);
        if (nameSpec != null) {
            specifications.add(nameSpec);
        }

        Specification<Treatment> activeSpec = byActive(active);
        if (activeSpec != null) {
            specifications.add(activeSpec);
        }

        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}