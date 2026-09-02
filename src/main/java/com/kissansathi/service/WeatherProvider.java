package com.kissansathi.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface WeatherProvider {

    Optional<CurrentWeather> fetchCurrent(
            BigDecimal latitude,
            BigDecimal longitude
    );

    record CurrentWeather(
            Float temperature,
            Float humidity,
            Float rainfall,
            Float windSpeed,
            Float pressure,
            String weatherCondition
    ) {
    }
}