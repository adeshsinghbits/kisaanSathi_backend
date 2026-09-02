package com.kissansathi.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class WeatherProviderImpl implements WeatherProvider {

    @Override
    public Optional<CurrentWeather> fetchCurrent(
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        /*
         * Live weather API abhi configured nahi hai.
         * Empty return hone par WeatherService
         * latest weather_history record use karega.
         */
        return Optional.empty();
    }
}