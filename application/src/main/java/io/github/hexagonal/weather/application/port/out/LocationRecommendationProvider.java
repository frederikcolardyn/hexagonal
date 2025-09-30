package io.github.hexagonal.weather.application.port.out;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.LocationRecommendation;

import java.util.List;

/**
 * Port for accessing location recommendation data.
 */
public interface LocationRecommendationProvider {

    /**
     * Finds recommended locations near the given location.
     *
     * @param location      Reference location
     * @param maxDistance   Maximum distance in hours
     * @param maxBudget     Maximum daily budget
     * @return List of matching recommendations
     */
    List<LocationRecommendation> findRecommendations(
        Location location,
        double maxDistance,
        double maxBudget
    );

    /**
     * Gets seasonal recommendations.
     *
     * @param season Current season
     * @param limit  Maximum results
     * @return List of recommendations
     */
    List<LocationRecommendation> findSeasonalRecommendations(String season, int limit);
}