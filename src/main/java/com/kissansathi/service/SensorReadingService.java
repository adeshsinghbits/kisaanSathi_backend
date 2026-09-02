package com.kissansathi.service;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.sensor.SensorReadingRequest;
import com.kissansathi.dto.sensor.SensorReadingResponse;
import org.springframework.data.domain.Pageable;

public interface SensorReadingService {
    SensorReadingResponse submitReading(SensorReadingRequest request);
    PagedResponse<SensorReadingResponse> getMyReadings(Pageable pageable);
    SensorReadingResponse getReadingById(Long id);
    PagedResponse<SensorReadingResponse> getReadingsForDevice(Long deviceId, Pageable pageable);
    SensorReadingResponse getLatestReadingForDevice(Long deviceId);
}