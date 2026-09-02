package com.kissansathi.service;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.mandi.MandiPriceResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MandiPriceService {
    PagedResponse<MandiPriceResponse> getLatest(Pageable pageable);
    MandiPriceResponse getById(Long id);
    PagedResponse<MandiPriceResponse> searchByCommodity(String commodity, Pageable pageable);
    PagedResponse<MandiPriceResponse> searchByDistrict(String district, Pageable pageable);
    PagedResponse<MandiPriceResponse> searchByState(String state, Pageable pageable);
    PagedResponse<MandiPriceResponse> searchByMarket(String market, Pageable pageable);
    PagedResponse<MandiPriceResponse> filterByArrivalDate(LocalDate date, Pageable pageable);
}