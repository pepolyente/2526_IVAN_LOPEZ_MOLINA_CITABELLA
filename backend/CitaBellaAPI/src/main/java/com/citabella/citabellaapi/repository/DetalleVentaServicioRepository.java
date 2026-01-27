package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.sale.DetalleVentaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaServicioRepository extends JpaRepository<DetalleVentaServicio, Integer> {
}
