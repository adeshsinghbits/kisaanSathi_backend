package com.kissansathi.service;

import com.kissansathi.entity.Crop;
import com.kissansathi.entity.SensorReading;

/**
 * Abstraction over the recommendation-generation strategy so that the initial rule-based
 * implementation can later be swapped for (or composed with) a FastAPI/ML-based provider
 * without any change to RecommendationService or the controller layer.
 *
 * Future architecture:
 * <pre>
 * RecommendationService -> RecommendationProvider
 *                              +-- RuleBasedRecommendationProvider (current)
 *                              +-- FastApiRecommendationProvider (future, calls out to a Python service)
 * </pre>
 */
public interface RecommendationProvider {
    GeneratedRecommendation generate(SensorReading reading, Crop crop);

    record GeneratedRecommendation(String recommendation, String fertilizer, String irrigation, float confidence) {
    }
}