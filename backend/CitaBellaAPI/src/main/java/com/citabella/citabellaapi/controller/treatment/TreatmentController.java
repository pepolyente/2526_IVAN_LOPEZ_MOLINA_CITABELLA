package com.citabella.citabellaapi.controller.treatment;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.filter.FilterRequest;
import com.citabella.citabellaapi.dto.page.PageResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentDetailedResponse;
import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.service.interfaces.TreatmentService;
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


@Tag(name = "Treatments", description = "Treatment management")
@RestController
@RequestMapping("api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Operation(summary = "Create treatment", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TreatmentResponse> create(@Valid @RequestBody TreatmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(treatmentService.create(request));
    }

    @Operation(summary = "Get treatment by ID", description = ApiSecurityDocs.ADMIN_EMPLOYEE_CLIENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponse> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(treatmentService.getById(id));
    }

    @Operation(
            summary = "Get all active treatments",
            description = ApiSecurityDocs.ANYONE)
    @GetMapping
    public ResponseEntity<PageResponse<TreatmentResponse>> findAllActive(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(PageResponse.from(
                treatmentService.findAll(pageable, true)));
    }

    @Operation(
            summary = "Get all treatments detailed (paginated, filterable by active)",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail")
    public ResponseEntity<PageResponse<TreatmentDetailedResponse>> findAllDetailed(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @ParameterObject FilterRequest filterRequest) {

        return ResponseEntity.ok(PageResponse.from(
                treatmentService.findAllDetailed(pageable, active, filterRequest)));
    }

    @Operation(summary = "Update treatment", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TreatmentRequest request) {

        return ResponseEntity.ok(treatmentService.update(id, request));
    }

    @Operation(
            summary = "Deactivate treatment (logical delete)",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<TreatmentResponse> deactivate(@PathVariable Integer id) {

        return ResponseEntity.ok(treatmentService.deactivate(id));
    }

    @Operation(summary = "Activate treatment", description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<TreatmentResponse> activate(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentService.activate(id));
    }
}
