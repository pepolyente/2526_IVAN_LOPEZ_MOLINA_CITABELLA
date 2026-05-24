package com.citabella.citabellaapi.repository.specifications;

import com.citabella.citabellaapi.entity.product.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private ProductSpecification() {
    }

    /**
     * Búsqueda LIKE por nombre de producto (case-insensitive).
     */
    public static Specification<Product> byName(String search) {
        return BaseSpecifications.byFieldLike(search, "name");
    }

    /**
     * Filtro por estado activo/inactivo.
     */
    public static Specification<Product> byActive(Boolean active) {
        return BaseSpecifications.byActive(active);
    }

    /**
     * Combina filtro por nombre y por active.
     */
    public static Specification<Product> withFilters(String search, Boolean active) {

        List<Specification<Product>> specifications = new ArrayList<>();

        Specification<Product> nameSpec = byName(search);
        if (nameSpec != null) {
            specifications.add(nameSpec);
        }

        Specification<Product> activeSpec = byActive(active);
        if (activeSpec != null) {
            specifications.add(activeSpec);
        }

        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}