
package com.kissansathi.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherHistoryResponse {
    private Long id;
    private Long farmId;
    private Float temperature;
    private Float humidity;
    private Float rainfall;
    private Float windSpeed;
    private Float pressure;
    private String weatherCondition;
    private LocalDateTime recordedAt;
}