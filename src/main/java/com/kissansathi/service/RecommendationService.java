package com.kissansathi.service;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.recommendation.GenerateRecommendationRequest;
import com.kissansathi.dto.recommendation.RecommendationResponse;
import com.kissansathi.entity.SensorReading;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendationService {
    RecommendationResponse generate(GenerateRecommendationRequest request);

    /** Best-effort automatic generation triggered right after a sensor reading is ingested. */
    void generateAutomatically(SensorReading reading);

    PagedResponse<RecommendationResponse> getMyRecommendations(Pageable pageable);
    RecommendationResponse getById(Long id);
    List<RecommendationResponse> getForReading(Long readingId);
}