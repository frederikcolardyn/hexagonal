package io.github.hexagonal.weather.application.service;

import io.github.hexagonal.weather.application.port.in.GetLocationRecommendationsUseCase;
import io.github.hexagonal.weather.application.port.out.LocationRecommendationProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.LocationRecommendation;
import lombok.extern.jbosslog.JBossLog;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Service for providing location recommendations.
 */
@JBossLog
public class LocationRecommendationService implements GetLocationRecommendationsUseCase {

    private final LocationRecommendationProvider provider;

    public LocationRecommendationService(LocationRecommendationProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<LocationRecommendation> getRecommendations(
        Location currentLocation,
        double maxTravelTime,
        double budgetPerDay
    ) {
        log.infof("Finding recommendations from location: %s with max travel time: %.1f hours and budget: %.2f EUR",
            currentLocation.cityName(), maxTravelTime, budgetPerDay);

        List<LocationRecommendation> recommendations = provider.findRecommendations(
            currentLocation,
            maxTravelTime,
            budgetPerDay
        );

        log.infof("Found %d recommendations", recommendations.size());
        return recommendations;
    }

    @Override
    public List<LocationRecommendation> getSeasonalRecommendations(int limit) {
        String season = getCurrentSeason();
        log.infof("Getting top %d recommendations for season: %s", limit, season);

        return provider.findSeasonalRecommendations(season, limit);
    }

    private String getCurrentSeason() {
        Month month = LocalDate.now().getMonth();
        return switch (month) {
            case DECEMBER, JANUARY, FEBRUARY -> "winter";
            case MARCH, APRIL, MAY -> "spring";
            case JUNE, JULY, AUGUST -> "summer";
            case SEPTEMBER, OCTOBER, NOVEMBER -> "autumn";
        };
    }
}