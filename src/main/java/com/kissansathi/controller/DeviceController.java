package com.kissansathi.controller;

import com.kissansathi.dto.common.ApiResponse;
import com.kissansathi.dto.device.*;
import com.kissansathi.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Register and manage soil-monitoring devices")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @Operation(summary = "Register a new device")
    public ResponseEntity<ApiResponse<DeviceResponse>> create(@Valid @RequestBody DeviceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Device registered successfully", deviceService.createDevice(request)));
    }

    @GetMapping
    @Operation(summary = "List my devices")
    public ResponseEntity<ApiResponse<List<DeviceResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success("Devices fetched successfully", deviceService.getMyDevices()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by id")
    public ResponseEntity<ApiResponse<DeviceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Device fetched successfully", deviceService.getDeviceById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update device details")
    public ResponseEntity<ApiResponse<DeviceResponse>> update(@PathVariable Long id, @Valid @RequestBody DeviceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Device updated successfully", deviceService.updateDevice(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok(ApiResponse.success("Device deleted successfully"));
    }

    @PostMapping("/{id}/heartbeat")
    @Operation(summary = "Record a device heartbeat (updates last_seen, battery, firmware, status=ONLINE)")
    public ResponseEntity<ApiResponse<DeviceResponse>> heartbeat(@PathVariable Long id, @Valid @RequestBody HeartbeatRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Heartbeat recorded", deviceService.recordHeartbeat(id, request)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Manually update device status")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody DeviceStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Device status updated", deviceService.updateStatus(id, request)));
    }
}