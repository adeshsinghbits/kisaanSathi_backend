package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.sensor.SensorReadingRequest;
import com.kissansathi.dto.sensor.SensorReadingResponse;
import com.kissansathi.service.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Sensor Readings", description = "ESP32 sensor ingestion and reading retrieval")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @PostMapping("/api/sensors/readings")
    @Operation(summary = "Submit a sensor reading from an ESP32 device (identified by deviceUid)")
    public ResponseEntity<ApiResponse<SensorReadingResponse>> submit(@Valid @RequestBody SensorReadingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sensor reading recorded successfully", sensorReadingService.submitReading(request)));
    }

    @GetMapping("/api/sensors/readings")
    @Operation(summary = "List all sensor readings for the authenticated farmer's devices (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<SensorReadingResponse>>> list(
            @PageableDefault(size = 20, sort = "recordedAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Sensor readings fetched successfully", sensorReadingService.getMyReadings(pageable)));
    }

    @GetMapping("/api/sensors/readings/{id}")
    @Operation(summary = "Get a specific sensor reading by id")
    public ResponseEntity<ApiResponse<SensorReadingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Sensor reading fetched successfully", sensorReadingService.getReadingById(id)));
    }

    @GetMapping("/api/devices/{deviceId}/readings")
    @Operation(summary = "List sensor readings for a specific device (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<SensorReadingResponse>>> listForDevice(
            @PathVariable Long deviceId, @PageableDefault(size = 20, sort = "recordedAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Device readings fetched successfully",
                sensorReadingService.getReadingsForDevice(deviceId, pageable)));
    }

    @GetMapping("/api/devices/{deviceId}/readings/latest")
    @Operation(summary = "Get the latest sensor reading for a specific device")
    public ResponseEntity<ApiResponse<SensorReadingResponse>> getLatestForDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(ApiResponse.success("Latest reading fetched successfully",
                sensorReadingService.getLatestReadingForDevice(deviceId)));
    }
}