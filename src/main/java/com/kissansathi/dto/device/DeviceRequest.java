package com.kissansathi.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRequest {

    @NotBlank(message = "Device UID is required")
    @Size(max = 100)
    private String deviceUid;

    @Size(max = 100)
    private String deviceName;

    /** Optional. If provided, the farm must belong to the requesting user. */
    private Long farmId;

    @Size(max = 50)
    private String firmwareVersion;
}
