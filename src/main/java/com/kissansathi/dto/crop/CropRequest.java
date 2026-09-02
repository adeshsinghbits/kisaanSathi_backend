package com.kissansathi.dto.crop;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CropRequest {

    @NotBlank(message = "Crop name is required")
    @Size(max = 100)
    private String cropName;

    @Size(max = 50)
    private String season;

    @PositiveOrZero(message = "Ideal nitrogen must be zero or positive")
    private Float idealNitrogen;

    @PositiveOrZero(message = "Ideal phosphorus must be zero or positive")
    private Float idealPhosphorus;

    @PositiveOrZero(message = "Ideal potassium must be zero or positive")
    private Float idealPotassium;

    @DecimalMin(value = "0.0", message = "pH must be between 0 and 14")
    @DecimalMax(value = "14.0", message = "pH must be between 0 and 14")
    private Float idealPhMin;

    @DecimalMin(value = "0.0", message = "pH must be between 0 and 14")
    @DecimalMax(value = "14.0", message = "pH must be between 0 and 14")
    private Float idealPhMax;
}
