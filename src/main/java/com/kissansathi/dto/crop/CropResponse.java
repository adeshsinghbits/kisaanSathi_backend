package com.kissansathi.dto.crop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropResponse {
    private Long id;
    private String cropName;
    private String season;
    private Float idealNitrogen;
    private Float idealPhosphorus;
    private Float idealPotassium;
    private Float idealPhMin;
    private Float idealPhMax;
}