package com.citabella.citabellaapi.controller.product;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.dto.product.ProductPrivateResponse;
import com.citabella.citabellaapi.dto.product.ProductPublicResponse;
import com.citabella.citabellaapi.dto.product.ProductRequest;
import com.citabella.citabellaapi.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products", description = "Products management")
@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductPrivateResponse> create(
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    @Operation(summary = "Get product by ID (private)", description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductPrivateResponse> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(
            summary = "Get all products for admin (paginated, filterable by active)",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/admin")
    public ResponseEntity<PageResponse<ProductPrivateResponse>> findAllAdmin(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @ParameterObject FilterRequest filterRequest) {

        return ResponseEntity.ok(PageResponse.from(
                productService.findAllAdmin(pageable, active, filterRequest)));
    }

    @Operation(summary = "Get all active products (public)", description = ApiSecurityDocs.ANYONE)
    @GetMapping
    public ResponseEntity<List<ProductPublicResponse>> findAllActive() {

        return ResponseEntity.ok(productService.findAllActive());
    }

    @Operation(summary = "Update product", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductPrivateResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.update(id, request));
    }

    @Operation(
            summary = "Deactivate product (logical delete)",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductPrivateResponse> deactivate(@PathVariable Integer id) {
        
        return ResponseEntity.ok(productService.deactivate(id));
    }
}
