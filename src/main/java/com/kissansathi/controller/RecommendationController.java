package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.recommendation.GenerateRecommendationRequest;
import com.kissansathi.dto.recommendation.RecommendationResponse;
import com.kissansathi.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Rule-based crop fertilizer/irrigation recommendation engine")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/api/recommendations/generate")
    @Operation(summary = "Generate a recommendation for a sensor reading against a named crop")
    public ResponseEntity<ApiResponse<RecommendationResponse>> generate(@Valid @RequestBody GenerateRecommendationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recommendation generated successfully", recommendationService.generate(request)));
    }

    @GetMapping("/api/recommendations")
    @Operation(summary = "List my recommendations (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<RecommendationResponse>>> list(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Recommendations fetched successfully", recommendationService.getMyRecommendations(pageable)));
    }

    @GetMapping("/api/recommendations/{id}")
    @Operation(summary = "Get a recommendation by id")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Recommendation fetched successfully", recommendationService.getById(id)));
    }

    @GetMapping("/api/readings/{readingId}/recommendations")
    @Operation(summary = "List all recommendations generated for a specific sensor reading")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getForReading(@PathVariable Long readingId) {
        return ResponseEntity.ok(ApiResponse.success("Recommendations fetched successfully", recommendationService.getForReading(readingId)));
    }
}