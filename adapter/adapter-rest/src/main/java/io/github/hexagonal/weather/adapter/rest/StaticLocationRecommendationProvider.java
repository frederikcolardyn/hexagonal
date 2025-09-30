package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.application.port.out.LocationRecommendationProvider;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.LocationRecommendation;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Static implementation of location recommendation provider.
 * Contains hardcoded recommendations for demonstration.
 */
@ApplicationScoped
public class StaticLocationRecommendationProvider implements LocationRecommendationProvider {

    private final List<LocationRecommendation> allRecommendations;

    public StaticLocationRecommendationProvider() {
        this.allRecommendations = initializeRecommendations();
    }

    @Override
    public List<LocationRecommendation> findRecommendations(
        Location location,
        double maxDistance,
        double maxBudget
    ) {
        return allRecommendations.stream()
            .filter(rec -> rec.travelTime() <= maxDistance)
            .filter(rec -> rec.averageCost() <= maxBudget)
            .sorted((a, b) -> Integer.compare(b.score(), a.score()))
            .collect(Collectors.toList());
    }

    @Override
    public List<LocationRecommendation> findSeasonalRecommendations(String season, int limit) {
        return allRecommendations.stream()
            .filter(rec -> rec.bestMonthsToVisit().contains(season))
            .sorted((a, b) -> Integer.compare(b.score(), a.score()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    private List<LocationRecommendation> initializeRecommendations() {
        List<LocationRecommendation> recommendations = new ArrayList<>();

        recommendations.add(new LocationRecommendation(
            new Location(48.8566, 2.3522, "Paris"),
            95,
            List.of("Eiffel Tower", "Louvre Museum", "Notre-Dame"),
            List.of("spring", "summer", "autumn"),
            120.0,
            3.5
        ));

        recommendations.add(new LocationRecommendation(
            new Location(41.9028, 12.4964, "Rome"),
            92,
            List.of("Colosseum", "Vatican", "Trevi Fountain"),
            List.of("spring", "autumn"),
            90.0,
            4.0
        ));

        recommendations.add(new LocationRecommendation(
            new Location(52.5200, 13.4050, "Berlin"),
            88,
            List.of("Brandenburg Gate", "Berlin Wall", "Museum Island"),
            List.of("spring", "summer", "autumn"),
            70.0,
            2.5
        ));

        recommendations.add(new LocationRecommendation(
            new Location(51.5074, -0.1278, "London"),
            90,
            List.of("Big Ben", "British Museum", "Tower Bridge"),
            List.of("spring", "summer", "autumn"),
            150.0,
            2.0
        ));

        recommendations.add(new LocationRecommendation(
            new Location(40.4168, -3.7038, "Madrid"),
            85,
            List.of("Prado Museum", "Royal Palace", "Retiro Park"),
            List.of("spring", "autumn"),
            80.0,
            5.0
        ));

        return recommendations;
    }
}