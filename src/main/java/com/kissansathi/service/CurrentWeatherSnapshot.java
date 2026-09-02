package com.kissansathi.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentWeatherSnapshot {

    private final Float temperature;
    private final Float humidity;
    private final Float rainfall;
    private final Float windSpeed;
    private final Float pressure;
    private final String weatherCondition;
}