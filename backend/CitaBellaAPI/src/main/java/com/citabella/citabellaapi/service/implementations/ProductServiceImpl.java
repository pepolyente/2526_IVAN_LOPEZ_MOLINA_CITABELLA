package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.entity.product.Product;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.repository.ProductRepository;
import com.citabella.citabellaapi.service.interfaces.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductPrivateResponse getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
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

    @Override
    public List<ProductPublicResponse> findAllActive() {
        List<Product> products = productRepository.findAllByActive(true);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products not found");
        }
        List<ProductPublicResponse> publicProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getActive()) {
                publicProducts.add(new ProductPublicResponse(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getSalePrice(),
                        product.getImageKey()
                ));
            }
        }
        return publicProducts;
    }
}