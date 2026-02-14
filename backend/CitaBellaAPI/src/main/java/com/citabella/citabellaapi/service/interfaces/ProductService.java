package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductService {
    ProductPrivateResponse getById(Integer id);

    List<ProductPublicResponse> findAllActive();
}
