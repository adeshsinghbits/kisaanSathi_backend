package com.kissansathi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensor_readings", indexes = {
        @Index(name = "idx_sensor_readings_device_id", columnList = "device_id"),
        @Index(name = "idx_sensor_readings_recorded_at", columnList = "recorded_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"device", "recommendations"})
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "nitrogen")
    private Float nitrogen;

    @Column(name = "phosphorus")
    private Float phosphorus;

    @Column(name = "potassium")
    private Float potassium;

    @Column(name = "soil_moisture")
    private Float soilMoisture;

    @Column(name = "soil_temperature")
    private Float soilTemperature;

    @Column(name = "air_temperature")
    private Float airTemperature;

    @Column(name = "humidity")
    private Float humidity;

    @Column(name = "ph")
    private Float ph;

    @Column(name = "recorded_at", nullable = false)
    @Builder.Default
    private LocalDateTime recordedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "reading", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AIRecommendation> recommendations = new ArrayList<>();
}