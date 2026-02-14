package com.citabella.citabellaapi.controller.product;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Products", description = "Products management")
@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get prodcut by ID",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE_CLIENT)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductPrivateResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(
            summary = "Get all active products",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE_CLIENT)
    @GetMapping
    public ResponseEntity<List<ProductPublicResponse>> findAllActive() {
        List<ProductPublicResponse> response = productService.findAllActive();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
