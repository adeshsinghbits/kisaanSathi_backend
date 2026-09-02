package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.crop.CropRequest;
import com.kissansathi.dto.crop.CropResponse;
import com.kissansathi.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * All endpoints here are matched by the "/api/admin/**" security rule, which requires ROLE_ADMIN
 * (see SecurityConfig). No additional per-method checks are required for role, but ownership
 * rules elsewhere (farms/devices/etc.) still apply independently in their own services.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Administrative operations (requires ROLE_ADMIN)")
public class AdminController {

    private final CropService cropService;

    @PostMapping("/crops")
    @Operation(summary = "Create a new crop reference entry")
    public ResponseEntity<ApiResponse<CropResponse>> createCrop(@Valid @RequestBody CropRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Crop created successfully", cropService.create(request)));
    }

    @PutMapping("/crops/{id}")
    @Operation(summary = "Update a crop reference entry")
    public ResponseEntity<ApiResponse<CropResponse>> updateCrop(@PathVariable Long id, @Valid @RequestBody CropRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Crop updated successfully", cropService.update(id, request)));
    }

    @DeleteMapping("/crops/{id}")
    @Operation(summary = "Delete a crop reference entry")
    public ResponseEntity<ApiResponse<Void>> deleteCrop(@PathVariable Long id) {
        cropService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Crop deleted successfully"));
    }
}