package com.kissansathi.service;

import com.kissansathi.dto.weather.WeatherHistoryResponse;
import com.kissansathi.entity.Farm;
import com.kissansathi.entity.WeatherHistory;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.WeatherHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherHistoryRepository weatherHistoryRepository;
    private final FarmService farmService;
    private final WeatherProvider weatherProvider;

    @Transactional(readOnly = true)
    public Page<WeatherHistoryResponse> getHistoryForFarm(
            Long userId,
            Long farmId,
            Pageable pageable
    ) {
        Farm farm = farmService.getOwnedFarmOrThrow(userId, farmId);

        return weatherHistoryRepository
                .findByFarmIdOrderByRecordedAtDesc(
                        farm.getId(),
                        pageable
                )
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public WeatherHistoryResponse getLatestForFarm(
            Long userId,
            Long farmId
    ) {
        Farm farm = farmService.getOwnedFarmOrThrow(userId, farmId);

        return weatherHistoryRepository
                .findFirstByFarmIdOrderByRecordedAtDesc(farm.getId())
                .map(this::toResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No weather history recorded for this farm yet"
                        )
                );
    }

    @Transactional
    public WeatherHistoryResponse getCurrentWeather(
            Long userId,
            Long farmId
    ) {
        Farm farm = farmService.getOwnedFarmOrThrow(userId, farmId);

        WeatherProvider.CurrentWeather snapshot =
                weatherProvider
                        .fetchCurrent(
                                farm.getLatitude(),
                                farm.getLongitude()
                        )
                        .orElse(null);

        if (snapshot == null) {

            log.debug(
                    "No live weather provider configured; " +
                            "falling back to latest stored history for farmId={}",
                    farmId
            );

            return getLatestForFarm(userId, farmId);
        }

        WeatherHistory history = WeatherHistory.builder()
                .farm(farm)
                .temperature(snapshot.temperature())
                .humidity(snapshot.humidity())
                .rainfall(snapshot.rainfall())
                .windSpeed(snapshot.windSpeed())
                .pressure(snapshot.pressure())
                .weatherCondition(snapshot.weatherCondition())
                .build();

        WeatherHistory saved =
                weatherHistoryRepository.save(history);

        return toResponse(saved);
    }

    private WeatherHistoryResponse toResponse(
            WeatherHistory history
    ) {
        return WeatherHistoryResponse.builder()
                .id(history.getId())
                .farmId(
                        history.getFarm() != null
                                ? history.getFarm().getId()
                                : null
                )
                .temperature(history.getTemperature())
                .humidity(history.getHumidity())
                .rainfall(history.getRainfall())
                .windSpeed(history.getWindSpeed())
                .pressure(history.getPressure())
                .weatherCondition(history.getWeatherCondition())
                .recordedAt(history.getRecordedAt())
                .build();
    }
}