package com.kissansathi.repository;

import com.kissansathi.entity.AIRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<AIRecommendation, Long> {
    List<AIRecommendation> findByReadingId(Long readingId);
    Page<AIRecommendation> findByReadingDeviceUserId(Long userId, Pageable pageable);
    Optional<AIRecommendation> findByIdAndReadingDeviceUserId(Long id, Long userId);
    List<AIRecommendation> findTop5ByReadingDeviceUserIdOrderByCreatedAtDesc(Long userId);
}