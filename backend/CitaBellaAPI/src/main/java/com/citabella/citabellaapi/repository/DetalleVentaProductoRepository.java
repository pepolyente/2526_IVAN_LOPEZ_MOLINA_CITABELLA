package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.sale.DetalleVentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaProductoRepository extends JpaRepository<DetalleVentaProducto, Integer> {
}
