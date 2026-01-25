package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.venta.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
