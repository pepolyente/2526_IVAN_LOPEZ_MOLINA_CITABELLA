package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.producto.Producto;
import com.citabella.citabellaapi.entity.producto.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    //revisar método
    Stock getStockByProducto(Producto producto);
}
