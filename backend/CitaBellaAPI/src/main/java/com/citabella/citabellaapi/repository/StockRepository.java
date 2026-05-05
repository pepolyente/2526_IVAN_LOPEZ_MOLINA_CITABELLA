package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.product.Product;
import com.citabella.citabellaapi.entity.product.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Stock findStockByProduct(Product product);
}
