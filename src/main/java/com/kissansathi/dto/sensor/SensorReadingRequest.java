package com.kissansathi.dto.sensor;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/** Payload posted by the ESP32 device (identified by deviceUid, not a user JWT). */
@Getter
@Setter
public class SensorReadingRequest {

    @NotBlank(message = "Device UID is required")
    private String deviceUid;

    @NotNull(message = "Nitrogen value is required")
    @PositiveOrZero(message = "Nitrogen must be zero or positive")
    private Float nitrogen;

    @NotNull(message = "Phosphorus value is required")
    @PositiveOrZero(message = "Phosphorus must be zero or positive")
    private Float phosphorus;

    @NotNull(message = "Potassium value is required")
    @PositiveOrZero(message = "Potassium must be zero or positive")
    private Float potassium;

    @NotNull(message = "Soil moisture is required")
    @DecimalMin(value = "0.0", message = "Soil moisture must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Soil moisture must be between 0 and 100")
    private Float soilMoisture;

    @DecimalMin(value = "-40.0")
    @DecimalMax(value = "80.0")
    private Float soilTemperature;

    @DecimalMin(value = "-40.0")
    @DecimalMax(value = "80.0")
    private Float airTemperature;

    @DecimalMin(value = "0.0", message = "Humidity must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Humidity must be between 0 and 100")
    private Float humidity;

    @NotNull(message = "pH value is required")
    @DecimalMin(value = "0.0", message = "pH must be between 0 and 14")
    @DecimalMax(value = "14.0", message = "pH must be between 0 and 14")
    private Float ph;
}
