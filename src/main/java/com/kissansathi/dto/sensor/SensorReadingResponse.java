package com.kissansathi.dto.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingResponse {
    private Long id;
    private Long deviceId;
    private String deviceUid;
    private Float nitrogen;
    private Float phosphorus;
    private Float potassium;
    private Float soilMoisture;
    private Float soilTemperature;
    private Float airTemperature;
    private Float humidity;
    private Float ph;
    private LocalDateTime recordedAt;
}