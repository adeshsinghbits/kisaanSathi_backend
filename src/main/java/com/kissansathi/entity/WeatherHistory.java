package com.kissansathi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_history", indexes = {
        @Index(name = "idx_weather_history_farm_id", columnList = "farm_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"farm"})
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "humidity")
    private Float humidity;

    @Column(name = "rainfall")
    private Float rainfall;

    @Column(name = "wind_speed")
    private Float windSpeed;

    @Column(name = "pressure")
    private Float pressure;

    @Column(name = "weather_condition", length = 100)
    private String weatherCondition;

    @Column(name = "recorded_at", nullable = false)
    @Builder.Default
    private LocalDateTime recordedAt = LocalDateTime.now();
}