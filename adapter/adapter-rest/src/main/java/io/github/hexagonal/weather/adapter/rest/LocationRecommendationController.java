package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.application.port.in.GetLocationRecommendationsUseCase;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.LocationRecommendation;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * REST endpoint for location recommendations.
 */
@Path("/recommendations")
@Produces(MediaType.APPLICATION_JSON)
public class LocationRecommendationController {

    private final GetLocationRecommendationsUseCase getLocationRecommendationsUseCase;

    @Inject
    public LocationRecommendationController(GetLocationRecommendationsUseCase getLocationRecommendationsUseCase) {
        this.getLocationRecommendationsUseCase = getLocationRecommendationsUseCase;
    }

    @GET
    public List<LocationRecommendation> getRecommendations(
        @QueryParam("lat") double latitude,
        @QueryParam("lon") double longitude,
        @QueryParam("city") String cityName,
        @QueryParam("maxTravelTime") double maxTravelTime,
        @QueryParam("budget") double budgetPerDay
    ) {
        Location currentLocation = cityName != null && !cityName.isBlank()
            ? new Location(latitude, longitude, cityName)
            : new Location(latitude, longitude);

        return getLocationRecommendationsUseCase.getRecommendations(
            currentLocation,
            maxTravelTime,
            budgetPerDay
        );
    }

    @GET
    @Path("/seasonal")
    public List<LocationRecommendation> getSeasonalRecommendations(
        @QueryParam("limit") int limit
    ) {
        int actualLimit = limit > 0 ? limit : 10;
        return getLocationRecommendationsUseCase.getSeasonalRecommendations(actualLimit);
    }
}