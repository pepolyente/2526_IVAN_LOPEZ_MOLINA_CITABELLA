package com.citabella.citabellaapi.mappers;

import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.entity.product.Product;

public class ProductMapper {
    public static ProductPrivateResponse toPrivateResponse(Product product) {
        if (product == null) return null;

        return new ProductPrivateResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPurchasePrice(),
                product.getSalePrice(),
                product.getSupplier(),
                product.getIsCritical(),
                product.getActive(),
                product.getImageKey()
        );
    }

    public static ProductPublicResponse toPublicResponse(Product product) {
        if (product == null) return null;

        return new ProductPublicResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getSalePrice(),
                product.getImageKey()
        );
    }
}
