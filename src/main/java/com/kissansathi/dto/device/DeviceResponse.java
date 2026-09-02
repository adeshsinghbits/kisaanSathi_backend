package com.kissansathi.dto.device;

import com.kissansathi.entity.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private Long id;
    private Long userId;
    private Long farmId;
    private String farmName;
    private String deviceUid;
    private String deviceName;
    private String firmwareVersion;
    private Integer batteryLevel;
    private DeviceStatus status;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
}