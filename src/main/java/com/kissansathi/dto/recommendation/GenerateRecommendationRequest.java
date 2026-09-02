package com.kissansathi.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Triggers recommendation generation for an existing sensor reading against a named crop. */
@Getter
@Setter
public class GenerateRecommendationRequest {

    @NotNull(message = "Sensor reading id is required")
    private Long readingId;

    @NotBlank(message = "Crop name is required")
    private String cropName;
}

