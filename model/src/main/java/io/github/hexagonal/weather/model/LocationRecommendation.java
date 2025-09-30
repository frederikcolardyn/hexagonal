package io.github.hexagonal.weather.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Domain model representing a location recommendation for travel.
 *
 * @param location          The recommended location
 * @param score             Recommendation score (0-100)
 * @param attractions       List of nearby attractions
 * @param bestMonthsToVisit Best months to visit this location
 * @param averageCost       Average daily cost in EUR
 * @param travelTime        Estimated travel time from reference point in hours
 */
public record LocationRecommendation(
    @NotNull @Valid Location location,
    @Min(0) @Max(100) int score,
    @NotNull List<String> attractions,
    @NotNull List<String> bestMonthsToVisit,
    @Min(0) double averageCost,
    @Min(0) double travelTime
) {
    /**
     * Determines if this is a highly recommended location.
     */
    public boolean isHighlyRecommended() {
        return score >= 80;
    }

    /**
     * Checks if location is affordable for budget travelers.
     */
    public boolean isBudgetFriendly() {
        return averageCost <= 50.0;
    }
}