package com.citabella.citabellaapi.service.implementations;

import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.dto.product.ProductRequest;
import com.citabella.citabellaapi.entity.product.Product;
import com.citabella.citabellaapi.exception.BadRequestException;
import com.citabella.citabellaapi.exception.ResourceNotFoundException;
import com.citabella.citabellaapi.mappers.ProductMapper;
import com.citabella.citabellaapi.repository.ProductRepository;
import com.citabella.citabellaapi.repository.specifications.ProductSpecification;
import com.citabella.citabellaapi.service.interfaces.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public ProductPrivateResponse create(ProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new BadRequestException("Product name already exists");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPurchasePrice(request.purchasePrice());
        product.setSalePrice(request.salePrice());
        if (request.usageType() != null) {
            product.setUsageType(request.usageType());
        }
        product.setSupplier(request.supplier());
        if (request.isCritical() != null) {
            product.setIsCritical(request.isCritical());
        }
        product.setImageKey(request.imageKey());

        return ProductMapper.toPrivateResponse(productRepository.save(product));
    }

    @Override
    public ProductPrivateResponse getById(Integer id) {
        return ProductMapper.toPrivateResponse(
                productRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    @Override
    public Page<ProductPrivateResponse> findAllAdmin(Pageable pageable, Boolean active, FilterRequest filterRequest) {
        String search = (filterRequest != null) ? filterRequest.search() : null;

        Specification<Product> spec = ProductSpecification.withFilters(search, active);

        return productRepository.findAll(spec, pageable)
                .map(ProductMapper::toPrivateResponse);
    }

    @Override
    public List<ProductPublicResponse> findAllActive() {
        return productRepository.findAllByActive(true).stream()
                .filter(Product::getActive)
                .map(ProductMapper::toPublicResponse)
                .toList();
    }

    @Override
    public ProductPrivateResponse update(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.name() != null && !request.name().isBlank()) {
            if (!request.name().equals(product.getName())
                    && productRepository.existsByName(request.name())) {
                throw new BadRequestException("Product name already exists");
            }
            product.setName(request.name());
        }
        if (request.category() != null) product.setCategory(request.category());
        if (request.purchasePrice() != null) product.setPurchasePrice(request.purchasePrice());
        if (request.salePrice() != null) product.setSalePrice(request.salePrice());
        if (request.usageType() != null) product.setUsageType(request.usageType());
        if (request.supplier() != null) product.setSupplier(request.supplier());
        if (request.isCritical() != null) product.setIsCritical(request.isCritical());
        if (request.imageKey() != null) product.setImageKey(request.imageKey());

        return ProductMapper.toPrivateResponse(productRepository.save(product));
    }

    @Override
    public ProductPrivateResponse deactivate(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getActive()) {
            throw new BadRequestException("Product is already inactive");
        }
        product.setActive(false);
        return ProductMapper.toPrivateResponse(productRepository.save(product));
    }
}