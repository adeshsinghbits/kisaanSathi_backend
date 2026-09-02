package com.kissansathi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_recommendations", indexes = {
        @Index(name = "idx_ai_recommendations_reading_id", columnList = "reading_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"reading"})
public class AIRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_id")
    private SensorReading reading;

    @Column(name = "crop_name", length = 100)
    private String cropName;

    @Lob
    @Column(name = "recommendation", columnDefinition = "TEXT")
    private String recommendation;

    @Lob
    @Column(name = "fertilizer", columnDefinition = "TEXT")
    private String fertilizer;

    @Lob
    @Column(name = "irrigation", columnDefinition = "TEXT")
    private String irrigation;

    @Column(name = "confidence")
    private Float confidence;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}