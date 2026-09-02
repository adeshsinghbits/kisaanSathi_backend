package com.kissansathi.dto.farm;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FarmRequest {

    @NotBlank(message = "Farm name is required")
    @Size(max = 100)
    private String farmName;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String village;

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
    private BigDecimal longitude;

    @NotNull(message = "Area in acres is required")
    @DecimalMin(value = "0.01", message = "Area must be greater than 0")
    private BigDecimal areaAcres;

    @Size(max = 100)
    private String soilType;
}