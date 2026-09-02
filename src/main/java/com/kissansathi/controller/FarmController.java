package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.farm.FarmRequest;
import com.kissansathi.dto.farm.FarmResponse;
import com.kissansathi.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
@RequiredArgsConstructor
@Tag(name = "Farms", description = "Manage a farmer's own farms")
public class FarmController {

    private final FarmService farmService;

    @PostMapping
    @Operation(summary = "Create a new farm")
    public ResponseEntity<ApiResponse<FarmResponse>> create(@Valid @RequestBody FarmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Farm created successfully", farmService.createFarm(request)));
    }

    @GetMapping
    @Operation(summary = "List my farms")
    public ResponseEntity<ApiResponse<List<FarmResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success("Farms fetched successfully", farmService.getMyFarms()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a farm by id (must be owned by the caller)")
    public ResponseEntity<ApiResponse<FarmResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Farm fetched successfully", farmService.getFarmById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a farm")
    public ResponseEntity<ApiResponse<FarmResponse>> update(@PathVariable Long id, @Valid @RequestBody FarmRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Farm updated successfully", farmService.updateFarm(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a farm")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        farmService.deleteFarm(id);
        return ResponseEntity.ok(ApiResponse.success("Farm deleted successfully"));
    }
}