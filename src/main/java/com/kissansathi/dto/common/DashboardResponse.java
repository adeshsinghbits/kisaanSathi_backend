package com.kissansathi.dto.common;

import com.kissansathi.dto.device.DeviceResponse;
import com.kissansathi.dto.farm.FarmResponse;
import com.kissansathi.dto.mandi.MandiPriceResponse;
import com.kissansathi.dto.recommendation.RecommendationResponse;
import com.kissansathi.dto.sensor.SensorReadingResponse;
import com.kissansathi.dto.weather.WeatherHistoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private List<FarmResponse> farms;
    private List<DeviceResponse> devices;
    private List<SensorReadingResponse> latestSensorReadings;
    private List<RecommendationResponse> latestRecommendations;
    private WeatherHistoryResponse weather;
    private List<MandiPriceResponse> mandiPrices;
    private long unreadNotifications;
}