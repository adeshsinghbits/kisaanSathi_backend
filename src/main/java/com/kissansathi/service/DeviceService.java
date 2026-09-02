package com.kissansathi.service;

import com.kissansathi.dto.device.DeviceRequest;
import com.kissansathi.dto.device.DeviceResponse;
import com.kissansathi.dto.device.DeviceStatusUpdateRequest;
import com.kissansathi.dto.device.HeartbeatRequest;

import java.util.List;

public interface DeviceService {
    DeviceResponse createDevice(DeviceRequest request);
    List<DeviceResponse> getMyDevices();
    DeviceResponse getDeviceById(Long id);
    DeviceResponse updateDevice(Long id, DeviceRequest request);
    void deleteDevice(Long id);
    DeviceResponse recordHeartbeat(Long id, HeartbeatRequest request);
    DeviceResponse updateStatus(Long id, DeviceStatusUpdateRequest request);
    void markStaleDevicesOffline();
}