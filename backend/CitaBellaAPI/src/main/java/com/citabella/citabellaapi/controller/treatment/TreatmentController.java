package com.citabella.citabellaapi.controller.treatment;

import com.citabella.citabellaapi.docs.ApiSecurityDocs;
import com.citabella.citabellaapi.dto.treatment.TreatmentRequest;
import com.citabella.citabellaapi.dto.treatment.TreatmentResponse;
import com.citabella.citabellaapi.service.interfaces.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Treatments", description = "Treatment management")
@RestController
@RequestMapping("api/treatments")
@RequiredArgsConstructor
public class TreatmentController {


    private final TreatmentService treatmentService;

    @Operation(
            summary = "Create treatment",
            description = ApiSecurityDocs.ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TreatmentResponse> create(@Valid @RequestBody TreatmentRequest request) {
        TreatmentResponse response = treatmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get treatment by ID",
            description = ApiSecurityDocs.ADMIN_EMPLOYEE_CLIENT)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(treatmentService.getById(id));
    }

    @Operation(
            summary = "Get all treatments",
            description = ApiSecurityDocs.ANYONE)
    @GetMapping
    public ResponseEntity<List<TreatmentResponse>> findAll() {
        return ResponseEntity.ok(treatmentService.findAllActive());
    }
}
