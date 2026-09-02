package com.kissansathi.service.impl;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.sensor.SensorReadingRequest;
import com.kissansathi.dto.sensor.SensorReadingResponse;
import com.kissansathi.entity.Device;
import com.kissansathi.entity.DeviceStatus;
import com.kissansathi.entity.SensorReading;
import com.kissansathi.exception.ForbiddenException;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.DeviceRepository;
import com.kissansathi.repository.SensorReadingRepository;
import com.kissansathi.security.SecurityUtils;
import com.kissansathi.service.RecommendationService;
import com.kissansathi.service.SensorReadingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorReadingServiceImpl implements SensorReadingService {

    private static final Logger log = LoggerFactory.getLogger(SensorReadingServiceImpl.class);

    private final SensorReadingRepository sensorReadingRepository;
    private final DeviceRepository deviceRepository;
    private final RecommendationService recommendationService;
    private final SecurityUtils securityUtils;

    /**
     * Entry point for the ESP32 device. Authenticated implicitly by deviceUid (a secret, unique
     * identifier flashed onto the device) rather than a user JWT, since the device cannot hold
     * per-user login credentials.
     */
    @Override
    @Transactional
    public SensorReadingResponse submitReading(SensorReadingRequest request) {
        Device device = deviceRepository.findByDeviceUid(request.getDeviceUid())
                .orElseThrow(() -> new ResourceNotFoundException("Unknown device UID: " + request.getDeviceUid()));

        SensorReading reading = SensorReading.builder()
                .device(device)
                .nitrogen(request.getNitrogen())
                .phosphorus(request.getPhosphorus())
                .potassium(request.getPotassium())
                .soilMoisture(request.getSoilMoisture())
                .soilTemperature(request.getSoilTemperature())
                .airTemperature(request.getAirTemperature())
                .humidity(request.getHumidity())
                .ph(request.getPh())
                .recordedAt(LocalDateTime.now())
                .build();

        SensorReading saved = sensorReadingRepository.save(reading);

        device.setLastSeen(LocalDateTime.now());
        device.setStatus(DeviceStatus.ONLINE);
        deviceRepository.save(device);

        log.info("Sensor reading stored: readingId={}, deviceUid={}", saved.getId(), device.getDeviceUid());

        // Optional hook for future automatic recommendation generation.
        recommendationService.generateAutomatically(saved);

        return toDto(saved);
    }

    @Override
    public PagedResponse<SensorReadingResponse> getMyReadings(Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        Page<SensorReading> page = sensorReadingRepository.findByDeviceUserId(userId, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public SensorReadingResponse getReadingById(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        SensorReading reading = securityUtils.isAdmin()
                ? sensorReadingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sensor reading not found"))
                : sensorReadingRepository.findByIdAndDeviceUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor reading not found"));
        return toDto(reading);
    }

    @Override
    public PagedResponse<SensorReadingResponse> getReadingsForDevice(Long deviceId, Pageable pageable) {
        assertDeviceOwnership(deviceId);
        Page<SensorReading> page = sensorReadingRepository.findByDeviceId(deviceId, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public SensorReadingResponse getLatestReadingForDevice(Long deviceId) {
        assertDeviceOwnership(deviceId);
        SensorReading reading = sensorReadingRepository.findFirstByDeviceIdOrderByRecordedAtDesc(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings found for this device"));
        return toDto(reading);
    }

    private void assertDeviceOwnership(Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        Long currentUserId = securityUtils.getCurrentUserId();
        if (!device.getUser().getId().equals(currentUserId) && !securityUtils.isAdmin()) {
            throw new ForbiddenException("You do not have access to this device");
        }
    }

    private SensorReadingResponse toDto(SensorReading reading) {
        return SensorReadingResponse.builder()
                .id(reading.getId())
                .deviceId(reading.getDevice().getId())
                .deviceUid(reading.getDevice().getDeviceUid())
                .nitrogen(reading.getNitrogen())
                .phosphorus(reading.getPhosphorus())
                .potassium(reading.getPotassium())
                .soilMoisture(reading.getSoilMoisture())
                .soilTemperature(reading.getSoilTemperature())
                .airTemperature(reading.getAirTemperature())
                .humidity(reading.getHumidity())
                .ph(reading.getPh())
                .recordedAt(reading.getRecordedAt())
                .build();
    }
}