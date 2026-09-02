package com.kissansathi.dto.device;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Sent periodically by the ESP32 (or the app on its behalf) to signal liveness. */
@Getter
@Setter
public class HeartbeatRequest {

    @Min(value = 0, message = "Battery level must be between 0 and 100")
    @Max(value = 100, message = "Battery level must be between 0 and 100")
    private Integer batteryLevel;

    @Size(max = 50)
    private String firmwareVersion;
}