package com.kissansathi.repository;

import com.kissansathi.entity.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    Page<SensorReading> findByDeviceId(Long deviceId, Pageable pageable);
    List<SensorReading> findByDeviceIdOrderByRecordedAtDesc(Long deviceId);
    Optional<SensorReading> findFirstByDeviceIdOrderByRecordedAtDesc(Long deviceId);
    Page<SensorReading> findByDeviceUserId(Long userId, Pageable pageable);
    Optional<SensorReading> findByIdAndDeviceUserId(Long id, Long userId);
}