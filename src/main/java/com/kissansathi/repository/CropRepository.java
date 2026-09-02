package com.kissansathi.repository;

import com.kissansathi.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByCropNameContainingIgnoreCase(String name);
    List<Crop> findBySeasonIgnoreCase(String season);
    Optional<Crop> findByCropNameIgnoreCase(String cropName);
}