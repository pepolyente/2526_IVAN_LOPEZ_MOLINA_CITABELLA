package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.producto.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Integer> {
}
