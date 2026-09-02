package com.kissansathi.repository;

import com.kissansathi.entity.WeatherHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherHistoryRepository extends JpaRepository<WeatherHistory, Long> {
    Page<WeatherHistory> findByFarmIdOrderByRecordedAtDesc(Long farmId, Pageable pageable);
    Optional<WeatherHistory> findFirstByFarmIdOrderByRecordedAtDesc(Long farmId);
}