package com.kissansathi.service;

import java.util.List;

/**
 * Abstraction point for ingesting mandi price data from an external source (e.g. Government of
 * India's Agmarknet/data.gov.in APIs). The current implementation relies solely on data already
 * present in the mandi_prices table; a future scheduled job could implement this interface to
 * pull and upsert fresh data without touching MandiPriceService or its controller.
 */
public interface MandiPriceProvider {
    List<MandiPriceRecord> fetchLatest(String commodity, String state);

    record MandiPriceRecord(String commodity, String market, String district, String state,
                            java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice,
                            java.math.BigDecimal modalPrice, java.time.LocalDate arrivalDate) {
    }
}