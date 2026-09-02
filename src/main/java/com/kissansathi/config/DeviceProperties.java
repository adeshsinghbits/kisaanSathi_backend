package com.kissansathi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.device")
@Getter
@Setter
public class DeviceProperties {
    /** Minutes of silence after which an ONLINE device is considered OFFLINE. */
    private int offlineThresholdMinutes = 15;
}