package com.kissansathi.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private Long id;
    private Long readingId;
    private String cropName;
    private String recommendation;
    private String fertilizer;
    private String irrigation;
    private Float confidence;
    private LocalDateTime createdAt;
}