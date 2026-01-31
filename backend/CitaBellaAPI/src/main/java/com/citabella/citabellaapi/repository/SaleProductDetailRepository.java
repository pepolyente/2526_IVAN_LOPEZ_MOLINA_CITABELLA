package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.sale.SaleProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductDetailRepository extends JpaRepository<SaleProductDetail, Integer> {
}
