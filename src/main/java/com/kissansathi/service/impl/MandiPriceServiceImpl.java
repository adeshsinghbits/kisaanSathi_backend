package com.kissansathi.service.impl;

import com.kissansathi.dto.common.PagedResponse;
import com.kissansathi.dto.mandi.MandiPriceResponse;
import com.kissansathi.entity.MandiPrice;
import com.kissansathi.exception.ResourceNotFoundException;
import com.kissansathi.repository.MandiPriceRepository;
import com.kissansathi.service.MandiPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MandiPriceServiceImpl implements MandiPriceService {

    private final MandiPriceRepository mandiPriceRepository;

    @Override
    public PagedResponse<MandiPriceResponse> getLatest(Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findAllByOrderByArrivalDateDesc(pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public MandiPriceResponse getById(Long id) {
        return toDto(mandiPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mandi price entry not found")));
    }

    @Override
    public PagedResponse<MandiPriceResponse> searchByCommodity(String commodity, Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findByCommodityContainingIgnoreCase(commodity, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public PagedResponse<MandiPriceResponse> searchByDistrict(String district, Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findByDistrictContainingIgnoreCase(district, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public PagedResponse<MandiPriceResponse> searchByState(String state, Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findByStateContainingIgnoreCase(state, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public PagedResponse<MandiPriceResponse> searchByMarket(String market, Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findByMarketContainingIgnoreCase(market, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    @Override
    public PagedResponse<MandiPriceResponse> filterByArrivalDate(LocalDate date, Pageable pageable) {
        Page<MandiPrice> page = mandiPriceRepository.findByArrivalDate(date, pageable);
        return new PagedResponse<>(page.map(this::toDto).getContent(), page);
    }

    private MandiPriceResponse toDto(MandiPrice price) {
        return MandiPriceResponse.builder()
                .id(price.getId())
                .commodity(price.getCommodity())
                .market(price.getMarket())
                .district(price.getDistrict())
                .state(price.getState())
                .minPrice(price.getMinPrice())
                .maxPrice(price.getMaxPrice())
                .modalPrice(price.getModalPrice())
                .arrivalDate(price.getArrivalDate())
                .build();
    }
}