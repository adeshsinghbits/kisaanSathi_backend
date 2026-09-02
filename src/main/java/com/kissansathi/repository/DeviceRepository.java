package com.kissansathi.repository;

import com.kissansathi.entity.Device;
import com.kissansathi.entity.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUserId(Long userId);
    Optional<Device> findByIdAndUserId(Long id, Long userId);
    Optional<Device> findByDeviceUid(String deviceUid);
    boolean existsByDeviceUid(String deviceUid);
    boolean existsByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("UPDATE Device d SET d.status = com.kissansathi.entity.DeviceStatus.OFFLINE " +
            "WHERE d.status = com.kissansathi.entity.DeviceStatus.ONLINE AND d.lastSeen < :cutoff")
    int markStaleDevicesOffline(@Param("cutoff") LocalDateTime cutoff);
}