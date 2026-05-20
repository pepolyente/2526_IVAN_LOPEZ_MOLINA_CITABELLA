package com.citabella.citabellaapi.service.interfaces;

import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.dto.product.ProductRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentDetailedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    // Admin CRUD
    ProductPrivateResponse create(ProductRequest request);

    ProductPrivateResponse getById(Integer id);

    Page<ProductPrivateResponse> findAllAdmin(Pageable pageable, Boolean active, FilterRequest filterRequest);
    ProductPrivateResponse update(Integer id, ProductRequest request);

    ProductPrivateResponse deactivate(Integer id);

    // Público (sin auth)
    List<ProductPublicResponse> findAllActive();
}
