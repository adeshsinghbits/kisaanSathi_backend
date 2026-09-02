package com.kissansathi.dto.farm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponse {
    private Long id;
    private Long userId;
    private String farmName;
    private String state;
    private String district;
    private String village;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal areaAcres;
    private String soilType;
    private LocalDateTime createdAt;
}