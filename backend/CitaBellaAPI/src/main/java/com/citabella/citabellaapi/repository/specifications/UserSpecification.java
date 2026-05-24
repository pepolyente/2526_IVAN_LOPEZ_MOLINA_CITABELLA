package com.citabella.citabellaapi.repository.specifications;

import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.security.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private UserSpecification() {
    }

    /**
     * Búsqueda LIKE por username (case-insensitive).
     * NOTA: La entidad User no tiene campo "name", usa "username".
     */
    public static Specification<User> byUsername(String search) {
        return BaseSpecifications.byFieldLike(search, "username");
    }

    /**
     * Filtro exacto por accountStatus.
     */
    public static Specification<User> byAccountStatus(AccountStatus accountStatus) {

        if (accountStatus == null) {
            return null;
        }

        return (root, query, cb) ->
                cb.equal(root.get("accountStatus"), accountStatus);
    }

    /**
     * Combina filtro por username y por accountStatus.
     */
    public static Specification<User> withFilters(String search, AccountStatus accountStatus) {

        List<Specification<User>> specifications = new ArrayList<>();

        Specification<User> usernameSpec = byUsername(search);
        if (usernameSpec != null) {
            specifications.add(usernameSpec);
        }

        Specification<User> accountStatusSpec = byAccountStatus(accountStatus);
        if (accountStatusSpec != null) {
            specifications.add(accountStatusSpec);
        }

        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}