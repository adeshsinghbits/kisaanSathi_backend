package com.kissansathi.service.impl;

import com.kissansathi.entity.Crop;
import com.kissansathi.entity.SensorReading;
import com.kissansathi.service.RecommendationProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Initial, transparent rule-based engine comparing a sensor reading's NPK/pH/moisture values
 * against a crop's ideal reference ranges.
 *
 * IMPORTANT: outputs are advisory guidance, not scientifically guaranteed fertilizer dosages.
 * This provider is intentionally simple and modular so it can later be replaced or augmented by
 * an ICAR-STCR based fertilizer calculation module or an external ML/FastAPI service
 * (see {@link com.kissansathi.service.RecommendationProvider}).
 */
@Component
public class RuleBasedRecommendationProvider implements RecommendationProvider {

    private static final float TOLERANCE_RATIO = 0.15f; // 15% band around the ideal value counts as "adequate"

    @Override
    public GeneratedRecommendation generate(SensorReading reading, Crop crop) {
        List<String> observations = new ArrayList<>();
        List<String> fertilizerAdvice = new ArrayList<>();
        int matchedFactors = 0;
        int totalFactors = 0;

        // Nitrogen
        if (reading.getNitrogen() != null && crop.getIdealNitrogen() != null) {
            totalFactors++;
            if (isBelow(reading.getNitrogen(), crop.getIdealNitrogen())) {
                observations.add("Nitrogen level is below the ideal range for " + crop.getCropName() + ".");
                fertilizerAdvice.add("Consider a nitrogen-rich fertilizer (e.g. urea) according to soil requirement.");
            } else if (isAbove(reading.getNitrogen(), crop.getIdealNitrogen())) {
                observations.add("Nitrogen level is above the ideal range; excess nitrogen may harm crop growth.");
                fertilizerAdvice.add("Reduce nitrogen application in the next cycle.");
            } else {
                observations.add("Nitrogen level is within the adequate range.");
                matchedFactors++;
            }
        }

        // Phosphorus
        if (reading.getPhosphorus() != null && crop.getIdealPhosphorus() != null) {
            totalFactors++;
            if (isBelow(reading.getPhosphorus(), crop.getIdealPhosphorus())) {
                observations.add("Phosphorus level is below the ideal range.");
                fertilizerAdvice.add("Consider a phosphorus-rich fertilizer (e.g. DAP/SSP).");
            } else if (isAbove(reading.getPhosphorus(), crop.getIdealPhosphorus())) {
                observations.add("Phosphorus level is above the ideal range.");
            } else {
                observations.add("Phosphorus level is within the adequate range.");
                matchedFactors++;
            }
        }

        // Potassium
        if (reading.getPotassium() != null && crop.getIdealPotassium() != null) {
            totalFactors++;
            if (isBelow(reading.getPotassium(), crop.getIdealPotassium())) {
                observations.add("Potassium level is below the ideal range.");
                fertilizerAdvice.add("Consider a potash-based fertilizer (e.g. MOP).");
            } else if (isAbove(reading.getPotassium(), crop.getIdealPotassium())) {
                observations.add("Potassium level is above the ideal range.");
            } else {
                observations.add("Potassium level is within the adequate range.");
                matchedFactors++;
            }
        }

        // pH
        String irrigationAdvice;
        if (reading.getPh() != null && crop.getIdealPhMin() != null && crop.getIdealPhMax() != null) {
            totalFactors++;
            if (reading.getPh() < crop.getIdealPhMin()) {
                observations.add("Soil pH is more acidic than ideal for " + crop.getCropName() + ".");
                fertilizerAdvice.add("Consider agricultural lime to raise soil pH gradually.");
            } else if (reading.getPh() > crop.getIdealPhMax()) {
                observations.add("Soil pH is more alkaline than ideal for " + crop.getCropName() + ".");
                fertilizerAdvice.add("Consider elemental sulphur or organic matter to lower soil pH gradually.");
            } else {
                observations.add("Soil pH is within the ideal range.");
                matchedFactors++;
            }
        }

        // Irrigation guidance from soil moisture
        if (reading.getSoilMoisture() != null) {
            if (reading.getSoilMoisture() < 30f) {
                irrigationAdvice = "Soil moisture is low. Irrigation is recommended soon.";
            } else if (reading.getSoilMoisture() > 80f) {
                irrigationAdvice = "Soil moisture is high. Avoid irrigation to prevent waterlogging.";
            } else {
                irrigationAdvice = "Soil moisture is adequate. No immediate irrigation required.";
            }
        } else {
            irrigationAdvice = "Soil moisture data not available; irrigation guidance cannot be generated.";
        }

        String recommendation = observations.isEmpty()
                ? "Insufficient sensor/crop reference data to generate a detailed recommendation."
                : String.join(" ", observations);

        String fertilizer = fertilizerAdvice.isEmpty()
                ? "Nutrient levels appear adequate; no additional fertilizer is suggested at this time."
                : String.join(" ", fertilizerAdvice);

        float confidence = totalFactors == 0 ? 0.3f : Math.round((matchedFactors == totalFactors
                                                                  ? 0.9f
                                                                  : 0.5f + (0.4f * matchedFactors / totalFactors)) * 100) / 100f;

        return new GeneratedRecommendation(recommendation, fertilizer, irrigationAdvice, confidence);
    }

    private boolean isBelow(float actual, float ideal) {
        return actual < ideal * (1 - TOLERANCE_RATIO);
    }

    private boolean isAbove(float actual, float ideal) {
        return actual > ideal * (1 + TOLERANCE_RATIO);
    }
}