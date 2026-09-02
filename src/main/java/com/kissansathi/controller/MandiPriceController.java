package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.mandi.MandiPriceResponse;
import com.kissansathi.service.MandiPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/mandi/prices")
@RequiredArgsConstructor
@Tag(name = "Mandi Prices", description = "Agricultural market (mandi) price lookups")
public class MandiPriceController {

    private final MandiPriceService mandiPriceService;

    @GetMapping
    @Operation(summary = "Get latest mandi prices (paginated, sortable by arrivalDate/modalPrice)")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> getLatest(
            @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.getLatest(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a mandi price entry by id")
    public ResponseEntity<ApiResponse<MandiPriceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Mandi price fetched successfully", mandiPriceService.getById(id)));
    }

    @GetMapping("/commodity/{commodity}")
    @Operation(summary = "Search mandi prices by commodity")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> byCommodity(
            @PathVariable String commodity, @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.searchByCommodity(commodity, pageable)));
    }

    @GetMapping("/district/{district}")
    @Operation(summary = "Search mandi prices by district")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> byDistrict(
            @PathVariable String district, @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.searchByDistrict(district, pageable)));
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Search mandi prices by state")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> byState(
            @PathVariable String state, @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.searchByState(state, pageable)));
    }

    @GetMapping("/market/{market}")
    @Operation(summary = "Search mandi prices by market")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> byMarket(
            @PathVariable String market, @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.searchByMarket(market, pageable)));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Filter mandi prices by arrival date (yyyy-MM-dd)")
    public ResponseEntity<ApiResponse<PagedResponse<MandiPriceResponse>>> byDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 20, sort = "arrivalDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Mandi prices fetched successfully", mandiPriceService.filterByArrivalDate(date, pageable)));
    }
}