package com.kissansathi.service.impl;

import com.kissansathi.dto.farm.FarmRequest;
import com.kissansathi.dto.farm.FarmResponse;
import com.kissansathi.entity.Farm;
import com.kissansathi.entity.User;
import com.kissansathi.exception.ForbiddenException;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.FarmRepository;
import com.kissansathi.security.SecurityUtils;
import com.kissansathi.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {

    private static final Logger log =
            LoggerFactory.getLogger(FarmServiceImpl.class);

    private final FarmRepository farmRepository;
    private final SecurityUtils securityUtils;

    /**
     * Create a new farm for the currently authenticated user.
     */
    @Override
    @Transactional
    public FarmResponse createFarm(FarmRequest request) {

        User user = securityUtils.getCurrentUser();

        Farm farm = Farm.builder()
                .user(user)
                .farmName(request.getFarmName())
                .state(request.getState())
                .district(request.getDistrict())
                .village(request.getVillage())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .areaAcres(request.getAreaAcres())
                .soilType(request.getSoilType())
                .build();

        Farm saved = farmRepository.save(farm);

        log.info(
                "Farm created: farmId={}, userId={}",
                saved.getId(),
                user.getId()
        );

        return toDto(saved);
    }

    /**
     * Get all farms owned by the currently authenticated user.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FarmResponse> getMyFarms() {

        Long userId = securityUtils.getCurrentUserId();

        return farmRepository
                .findByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Get a farm by ID after checking ownership.
     */
    @Override
    @Transactional(readOnly = true)
    public FarmResponse getFarmById(Long id) {

        Farm farm = findOwnedFarmOrThrow(id);

        return toDto(farm);
    }

    /**
     * Update a farm after checking ownership.
     */
    @Override
    @Transactional
    public FarmResponse updateFarm(
            Long id,
            FarmRequest request
    ) {

        Farm farm = findOwnedFarmOrThrow(id);

        farm.setFarmName(request.getFarmName());
        farm.setState(request.getState());
        farm.setDistrict(request.getDistrict());
        farm.setVillage(request.getVillage());
        farm.setLatitude(request.getLatitude());
        farm.setLongitude(request.getLongitude());
        farm.setAreaAcres(request.getAreaAcres());
        farm.setSoilType(request.getSoilType());

        Farm updated = farmRepository.save(farm);

        log.info(
                "Farm updated: farmId={}, userId={}",
                updated.getId(),
                securityUtils.getCurrentUserId()
        );

        return toDto(updated);
    }

    /**
     * Delete a farm after checking ownership.
     */
    @Override
    @Transactional
    public void deleteFarm(Long id) {

        Farm farm = findOwnedFarmOrThrow(id);

        farmRepository.delete(farm);

        log.info(
                "Farm deleted: farmId={}, userId={}",
                id,
                securityUtils.getCurrentUserId()
        );
    }

    /**
     * Used by other services such as WeatherService.
     *
     * Checks that the requested farm belongs to the supplied user.
     * Admins are allowed to access any farm.
     */
    @Override
    @Transactional(readOnly = true)
    public Farm getOwnedFarmOrThrow(Long userId, Long farmId) {

        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farm not found")
                );

        Long ownerId = farm.getUser().getId();

        if (!ownerId.equals(userId) && !securityUtils.isAdmin()) {
            throw new ForbiddenException(
                    "You do not have access to this farm"
            );
        }

        return farm;
    }

    /**
     * Ownership check for operations performed by the
     * currently authenticated user.
     *
     * Admins can access any farm.
     */
    private Farm findOwnedFarmOrThrow(Long farmId) {

        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farm not found"
                        )
                );

        Long currentUserId =
                securityUtils.getCurrentUserId();

        Long ownerId =
                farm.getUser().getId();

        if (!ownerId.equals(currentUserId)
                && !securityUtils.isAdmin()) {

            throw new ForbiddenException(
                    "You do not have access to this farm"
            );
        }

        return farm;
    }

    /**
     * Convert Farm entity to FarmResponse DTO.
     */
    private FarmResponse toDto(Farm farm) {

        return FarmResponse.builder()
                .id(farm.getId())
                .userId(farm.getUser().getId())
                .farmName(farm.getFarmName())
                .state(farm.getState())
                .district(farm.getDistrict())
                .village(farm.getVillage())
                .latitude(farm.getLatitude())
                .longitude(farm.getLongitude())
                .areaAcres(farm.getAreaAcres())
                .soilType(farm.getSoilType())
                .createdAt(farm.getCreatedAt())
                .build();
    }
}