package com.kissansathi.service;

import com.kissansathi.dto.farm.FarmRequest;
import com.kissansathi.dto.farm.FarmResponse;
import com.kissansathi.entity.Farm;

import java.util.List;

public interface FarmService {

    FarmResponse createFarm(FarmRequest request);

    List<FarmResponse> getMyFarms();

    FarmResponse getFarmById(Long id);

    FarmResponse updateFarm(
            Long id,
            FarmRequest request
    );

    void deleteFarm(Long id);

    /**
     * Returns the farm only if it belongs to the given user
     * or the current authenticated user is an admin.
     */
    Farm getOwnedFarmOrThrow(
            Long userId,
            Long farmId
    );
}