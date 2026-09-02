package com.kissansathi.service.impl;

import com.kissansathi.dto.crop.CropRequest;
import com.kissansathi.dto.crop.CropResponse;
import com.kissansathi.entity.Crop;
import com.kissansathi.exception.ConflictException;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.CropRepository;
import com.kissansathi.service.CropService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private static final Logger log = LoggerFactory.getLogger(CropServiceImpl.class);

    private final CropRepository cropRepository;

    @Override
    public List<CropResponse> getAll() {
        return cropRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public CropResponse getById(Long id) {
        return toDto(findOrThrow(id));
    }

    @Override
    public List<CropResponse> searchByName(String name) {
        return cropRepository.findByCropNameContainingIgnoreCase(name).stream().map(this::toDto).toList();
    }

    @Override
    public List<CropResponse> filterBySeason(String season) {
        return cropRepository.findBySeasonIgnoreCase(season).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public CropResponse create(CropRequest request) {
        cropRepository.findByCropNameIgnoreCase(request.getCropName()).ifPresent(c -> {
            throw new ConflictException("A crop with this name already exists");
        });

        Crop crop = Crop.builder()
                .cropName(request.getCropName())
                .season(request.getSeason())
                .idealNitrogen(request.getIdealNitrogen())
                .idealPhosphorus(request.getIdealPhosphorus())
                .idealPotassium(request.getIdealPotassium())
                .idealPhMin(request.getIdealPhMin())
                .idealPhMax(request.getIdealPhMax())
                .build();

        Crop saved = cropRepository.save(crop);
        log.info("Crop created by admin: cropId={}, name={}", saved.getId(), saved.getCropName());
        return toDto(saved);
    }

    @Override
    @Transactional
    public CropResponse update(Long id, CropRequest request) {
        Crop crop = findOrThrow(id);
        crop.setCropName(request.getCropName());
        crop.setSeason(request.getSeason());
        crop.setIdealNitrogen(request.getIdealNitrogen());
        crop.setIdealPhosphorus(request.getIdealPhosphorus());
        crop.setIdealPotassium(request.getIdealPotassium());
        crop.setIdealPhMin(request.getIdealPhMin());
        crop.setIdealPhMax(request.getIdealPhMax());

        Crop saved = cropRepository.save(crop);
        log.info("Crop updated by admin: cropId={}", saved.getId());
        return toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Crop crop = findOrThrow(id);
        cropRepository.delete(crop);
        log.info("Crop deleted by admin: cropId={}", id);
    }

    private Crop findOrThrow(Long id) {
        return cropRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
    }

    private CropResponse toDto(Crop crop) {
        return CropResponse.builder()
                .id(crop.getId())
                .cropName(crop.getCropName())
                .season(crop.getSeason())
                .idealNitrogen(crop.getIdealNitrogen())
                .idealPhosphorus(crop.getIdealPhosphorus())
                .idealPotassium(crop.getIdealPotassium())
                .idealPhMin(crop.getIdealPhMin())
                .idealPhMax(crop.getIdealPhMax())
                .build();
    }
}