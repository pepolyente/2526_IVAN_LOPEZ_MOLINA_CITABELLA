package com.citabella.citabellaapi.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecifications {

    private BaseSpecifications() {
    }

    public static <T> Specification<T> byFieldLike(String value, String field) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(field)),
                        "%" + value.toLowerCase() + "%"
                );
    }

    public static <T> Specification<T> byActive(Boolean active) {

        if (active == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), active);
    }
}