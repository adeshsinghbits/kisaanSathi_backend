package com.kissansathi.service;

import com.kissansathi.dto.crop.CropRequest;
import com.kissansathi.dto.crop.CropResponse;

import java.util.List;

public interface CropService {
    List<CropResponse> getAll();
    CropResponse getById(Long id);
    List<CropResponse> searchByName(String name);
    List<CropResponse> filterBySeason(String season);

    // Admin-only mutations
    CropResponse create(CropRequest request);
    CropResponse update(Long id, CropRequest request);
    void delete(Long id);
}