package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByActive(Boolean active);

    Page<Product> findAllByActive(Boolean active, Pageable pageable);

    boolean existsByName(String name);
}
