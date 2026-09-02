package com.kissansathi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mandi_prices", indexes = {
        @Index(name = "idx_mandi_commodity", columnList = "commodity"),
        @Index(name = "idx_mandi_district", columnList = "district"),
        @Index(name = "idx_mandi_state", columnList = "state"),
        @Index(name = "idx_mandi_market", columnList = "market"),
        @Index(name = "idx_mandi_arrival_date", columnList = "arrival_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandiPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commodity", length = 100)
    private String commodity;

    @Column(name = "market", length = 100)
    private String market;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "min_price", precision = 10, scale = 2)
    private BigDecimal minPrice;

    @Column(name = "max_price", precision = 10, scale = 2)
    private BigDecimal maxPrice;

    @Column(name = "modal_price", precision = 10, scale = 2)
    private BigDecimal modalPrice;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
}