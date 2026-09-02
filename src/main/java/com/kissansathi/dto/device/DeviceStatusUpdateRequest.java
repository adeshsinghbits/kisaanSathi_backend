package com.kissansathi.dto.device;

import com.kissansathi.entity.DeviceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private DeviceStatus status;
}