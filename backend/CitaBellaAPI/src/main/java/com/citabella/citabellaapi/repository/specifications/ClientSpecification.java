package com.citabella.citabellaapi.repository.specifications;

import com.citabella.citabellaapi.entity.client.Client;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    private ClientSpecification() {
    }

    public static Specification<Client> byName(String search) {
        return BaseSpecifications.byFieldLike(search, "name");
    }

    public static Specification<Client> byActive(Boolean active) {
        return BaseSpecifications.byActive(active);
    }

    public static Specification<Client> withFilters(String search, Boolean active) {

        List<Specification<Client>> specifications = new ArrayList<>();

        Specification<Client> nameSpec = byName(search);
        if (nameSpec != null) {
            specifications.add(nameSpec);
        }

        Specification<Client> activeSpec = byActive(active);
        if (activeSpec != null) {
            specifications.add(activeSpec);
        }

        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}