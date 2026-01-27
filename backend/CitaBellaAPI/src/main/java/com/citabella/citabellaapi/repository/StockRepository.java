package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.product.Producto;
import com.citabella.citabellaapi.entity.product.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    //revisar método
    Stock getStockByProducto(Producto producto);
}
