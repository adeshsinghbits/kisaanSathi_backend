package com.kissansathi.service.impl;

import com.kissansathi.config.DeviceProperties;
import com.kissansathi.dto.device.DeviceRequest;
import com.kissansathi.dto.device.DeviceResponse;
import com.kissansathi.dto.device.DeviceStatusUpdateRequest;
import com.kissansathi.dto.device.HeartbeatRequest;
import com.kissansathi.entity.Device;
import com.kissansathi.entity.DeviceStatus;
import com.kissansathi.entity.Farm;
import com.kissansathi.entity.User;
import com.kissansathi.exception.ConflictException;
import com.kissansathi.exception.ForbiddenException;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.DeviceRepository;
import com.kissansathi.repository.FarmRepository;
import com.kissansathi.security.SecurityUtils;
import com.kissansathi.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;
    private final FarmRepository farmRepository;
    private final SecurityUtils securityUtils;
    private final DeviceProperties deviceProperties;

    @Override
    @Transactional
    public DeviceResponse createDevice(DeviceRequest request) {
        User user = securityUtils.getCurrentUser();

        if (deviceRepository.existsByDeviceUid(request.getDeviceUid())) {
            throw new ConflictException("A device with this UID is already registered");
        }

        Farm farm = resolveOwnedFarmOrNull(request.getFarmId(), user.getId());

        Device device = Device.builder()
                .user(user)
                .farm(farm)
                .deviceUid(request.getDeviceUid())
                .deviceName(request.getDeviceName())
                .firmwareVersion(request.getFirmwareVersion())
                .status(DeviceStatus.OFFLINE)
                .build();

        Device saved = deviceRepository.save(device);
        log.info("Device registered: deviceId={}, uid={}", saved.getId(), saved.getDeviceUid());
        return toDto(saved);
    }

    @Override
    public List<DeviceResponse> getMyDevices() {
        Long userId = securityUtils.getCurrentUserId();
        return deviceRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public DeviceResponse getDeviceById(Long id) {
        return toDto(findOwnedDeviceOrThrow(id));
    }

    @Override
    @Transactional
    public DeviceResponse updateDevice(Long id, DeviceRequest request) {
        Device device = findOwnedDeviceOrThrow(id);

        if (!device.getDeviceUid().equals(request.getDeviceUid())
                && deviceRepository.existsByDeviceUid(request.getDeviceUid())) {
            throw new ConflictException("A device with this UID is already registered");
        }

        Farm farm = resolveOwnedFarmOrNull(request.getFarmId(), device.getUser().getId());

        device.setDeviceUid(request.getDeviceUid());
        device.setDeviceName(request.getDeviceName());
        device.setFirmwareVersion(request.getFirmwareVersion());
        device.setFarm(farm);

        return toDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {
        Device device = findOwnedDeviceOrThrow(id);
        deviceRepository.delete(device);
        log.info("Device deleted: deviceId={}", id);
    }

    @Override
    @Transactional
    public DeviceResponse recordHeartbeat(Long id, HeartbeatRequest request) {
        Device device = findOwnedDeviceOrThrow(id);

        if (request.getBatteryLevel() != null) {
            device.setBatteryLevel(request.getBatteryLevel());
        }
        if (request.getFirmwareVersion() != null && !request.getFirmwareVersion().isBlank()) {
            device.setFirmwareVersion(request.getFirmwareVersion());
        }
        device.setLastSeen(LocalDateTime.now());
        device.setStatus(DeviceStatus.ONLINE);

        return toDto(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public DeviceResponse updateStatus(Long id, DeviceStatusUpdateRequest request) {
        Device device = findOwnedDeviceOrThrow(id);
        device.setStatus(request.getStatus());
        return toDto(deviceRepository.save(device));
    }

    /**
     * Scheduled sweep: any device that has been ONLINE but silent for longer than the configured
     * threshold is flipped to OFFLINE. Runs every minute; threshold is configurable via
     * app.device.offline-threshold-minutes (DEVICE_OFFLINE_THRESHOLD_MINUTES).
     */
    @Override
    @Transactional
    public void markStaleDevicesOffline() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(deviceProperties.getOfflineThresholdMinutes());
        int updated = deviceRepository.markStaleDevicesOffline(cutoff);
        if (updated > 0) {
            log.info("Marked {} stale device(s) OFFLINE (cutoff={})", updated, cutoff);
        }
    }

    private Farm resolveOwnedFarmOrNull(Long farmId, Long userId) {
        if (farmId == null) {
            return null;
        }
        return farmRepository.findByIdAndUserId(farmId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found or not owned by this user"));
    }

    private Device findOwnedDeviceOrThrow(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));

        Long currentUserId = securityUtils.getCurrentUserId();
        if (!device.getUser().getId().equals(currentUserId) && !securityUtils.isAdmin()) {
            throw new ForbiddenException("You do not have access to this device");
        }
        return device;
    }

    private DeviceResponse toDto(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .userId(device.getUser().getId())
                .farmId(device.getFarm() != null ? device.getFarm().getId() : null)
                .farmName(device.getFarm() != null ? device.getFarm().getFarmName() : null)
                .deviceUid(device.getDeviceUid())
                .deviceName(device.getDeviceName())
                .firmwareVersion(device.getFirmwareVersion())
                .batteryLevel(device.getBatteryLevel())
                .status(device.getStatus())
                .lastSeen(device.getLastSeen())
                .createdAt(device.getCreatedAt())
                .build();
    }
}