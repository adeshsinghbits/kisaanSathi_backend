package com.kissansathi.repository;

import com.kissansathi.entity.MandiPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MandiPriceRepository extends JpaRepository<MandiPrice, Long> {
    Page<MandiPrice> findByCommodityContainingIgnoreCase(String commodity, Pageable pageable);
    Page<MandiPrice> findByDistrictContainingIgnoreCase(String district, Pageable pageable);
    Page<MandiPrice> findByStateContainingIgnoreCase(String state, Pageable pageable);
    Page<MandiPrice> findByMarketContainingIgnoreCase(String market, Pageable pageable);
    Page<MandiPrice> findByArrivalDate(LocalDate arrivalDate, Pageable pageable);
    Page<MandiPrice> findAllByOrderByArrivalDateDesc(Pageable pageable);
}