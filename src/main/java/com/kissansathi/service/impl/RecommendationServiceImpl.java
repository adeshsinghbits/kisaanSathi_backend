package com.kissansathi.service.impl;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.recommendation.GenerateRecommendationRequest;
import com.kissansathi.dto.recommendation.RecommendationResponse;
import com.kissansathi.entity.AIRecommendation;
import com.kissansathi.entity.Crop;
import com.kissansathi.entity.SensorReading;
import com.kissansathi.exception.ForbiddenException;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.CropRepository;
import com.kissansathi.repository.RecommendationRepository;
import com.kissansathi.repository.SensorReadingRepository;
import com.kissansathi.security.SecurityUtils;
import com.kissansathi.service.RecommendationProvider;
import com.kissansathi.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository recommendationRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final CropRepository cropRepository;
    private final RecommendationProvider recommendationProvider;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public RecommendationResponse generate(GenerateRecommendationRequest request) {
        SensorReading reading = sensorReadingRepository.findById(request.getReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor reading not found"));

        Long currentUserId = securityUtils.getCurrentUserId();
        if (!reading.getDevice().getUser().getId().equals(currentUserId) && !securityUtils.isAdmin()) {
            throw new ForbiddenException("You do not have access to this sensor reading");
        }

        Crop crop = cropRepository.findByCropNameIgnoreCase(request.getCropName())
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found: " + request.getCropName()));

        AIRecommendation saved = buildAndSave(reading, crop);
        return toDto(saved);
    }

    @Override
    @Transactional
    public void generateAutomatically(SensorReading reading) {
        // Best effort: only proceeds if we can uniquely determine a crop is not implied,
        // so automatic generation is skipped unless a default/seasonal crop resolution strategy
        // is configured. Kept as a safe no-op hook for future ESP32-driven auto-recommendations,
        // e.g. wiring farm.soilType/season -> crop lookup.
        log.debug("Automatic recommendation generation is not configured for reading {}", reading.getId());
    }

    @Override
    public PagedResponse<RecommendationResponse> getMyRecommendations(Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        Page<AIRecommendation> page = recommendationRepository.findByReadingDeviceUserId(userId, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public RecommendationResponse getById(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        AIRecommendation recommendation = securityUtils.isAdmin()
                ? recommendationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recommendation not found"))
                : recommendationRepository.findByIdAndReadingDeviceUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found"));
        return toDto(recommendation);
    }

    @Override
    public List<RecommendationResponse> getForReading(Long readingId) {
        SensorReading reading = sensorReadingRepository.findById(readingId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor reading not found"));

        Long currentUserId = securityUtils.getCurrentUserId();
        if (!reading.getDevice().getUser().getId().equals(currentUserId) && !securityUtils.isAdmin()) {
            throw new ForbiddenException("You do not have access to this sensor reading");
        }

        return recommendationRepository.findByReadingId(readingId).stream().map(this::toDto).toList();
    }

    private AIRecommendation buildAndSave(SensorReading reading, Crop crop) {
        RecommendationProvider.GeneratedRecommendation generated = recommendationProvider.generate(reading, crop);

        AIRecommendation recommendation = AIRecommendation.builder()
                .reading(reading)
                .cropName(crop.getCropName())
                .recommendation(generated.recommendation())
                .fertilizer(generated.fertilizer())
                .irrigation(generated.irrigation())
                .confidence(generated.confidence())
                .build();

        AIRecommendation saved = recommendationRepository.save(recommendation);
        log.info("Recommendation generated: id={}, readingId={}, crop={}", saved.getId(), reading.getId(), crop.getCropName());
        return saved;
    }

    private RecommendationResponse toDto(AIRecommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .readingId(recommendation.getReading() != null ? recommendation.getReading().getId() : null)
                .cropName(recommendation.getCropName())
                .recommendation(recommendation.getRecommendation())
                .fertilizer(recommendation.getFertilizer())
                .irrigation(recommendation.getIrrigation())
                .confidence(recommendation.getConfidence())
                .createdAt(recommendation.getCreatedAt())
                .build();
    }
}