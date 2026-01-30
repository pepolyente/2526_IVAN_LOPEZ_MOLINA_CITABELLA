package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Sale, Integer> {
}
