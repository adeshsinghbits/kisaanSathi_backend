package com.kissansathi.util;

import com.kissansathi.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically flips devices that have stopped sending heartbeats from ONLINE to OFFLINE.
 * The staleness threshold is configurable (see DeviceProperties / DEVICE_OFFLINE_THRESHOLD_MINUTES).
 */
@Component
@RequiredArgsConstructor
public class DeviceOfflineScheduler {

    private final DeviceService deviceService;

    @Scheduled(fixedDelayString = "PT1M", initialDelayString = "PT1M")
    public void sweepStaleDevices() {
        deviceService.markStaleDevicesOffline();
    }
}