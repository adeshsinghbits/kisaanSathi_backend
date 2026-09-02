package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.crop.CropResponse;
import com.kissansathi.service.CropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
@Tag(name = "Crops", description = "Read-only crop reference data for farmers")
public class CropController {

    private final CropService cropService;

    @GetMapping
    @Operation(summary = "List all crops")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Crops fetched successfully", cropService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a crop by id")
    public ResponseEntity<ApiResponse<CropResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Crop fetched successfully", cropService.getById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search crops by (partial) name")
    public ResponseEntity<ApiResponse<List<CropResponse>>> search(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success("Crops fetched successfully", cropService.searchByName(name)));
    }

    @GetMapping("/season/{season}")
    @Operation(summary = "Filter crops by season")
    public ResponseEntity<ApiResponse<List<CropResponse>>> filterBySeason(@PathVariable String season) {
        return ResponseEntity.ok(ApiResponse.success("Crops fetched successfully", cropService.filterBySeason(season)));
    }
}