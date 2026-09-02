package com.kissansathi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crops", indexes = {
        @Index(name = "idx_crops_name", columnList = "crop_name"),
        @Index(name = "idx_crops_season", columnList = "season")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crop_name", length = 100)
    private String cropName;

    @Column(name = "season", length = 50)
    private String season;

    @Column(name = "ideal_nitrogen")
    private Float idealNitrogen;

    @Column(name = "ideal_phosphorus")
    private Float idealPhosphorus;

    @Column(name = "ideal_potassium")
    private Float idealPotassium;

    @Column(name = "ideal_ph_min")
    private Float idealPhMin;

    @Column(name = "ideal_ph_max")
    private Float idealPhMax;
}