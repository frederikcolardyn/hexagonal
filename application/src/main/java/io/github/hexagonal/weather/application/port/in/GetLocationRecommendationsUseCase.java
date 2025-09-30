package io.github.hexagonal.weather.application.port.in;

import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.LocationRecommendation;

import java.util.List;

/**
 * Use case for getting location recommendations based on weather and travel criteria.
 */
public interface GetLocationRecommendationsUseCase {

    /**
     * Gets recommended locations based on current weather patterns.
     *
     * @param currentLocation User's current location
     * @param maxTravelTime   Maximum travel time in hours
     * @param budgetPerDay    Maximum budget per day in EUR
     * @return List of recommended locations
     */
    List<LocationRecommendation> getRecommendations(
        Location currentLocation,
        double maxTravelTime,
        double budgetPerDay
    );

    /**
     * Gets top recommended destinations for the current season.
     *
     * @param limit Maximum number of recommendations
     * @return List of top recommended locations
     */
    List<LocationRecommendation> getSeasonalRecommendations(int limit);
}