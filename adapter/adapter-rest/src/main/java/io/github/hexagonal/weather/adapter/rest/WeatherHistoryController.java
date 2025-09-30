package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.adapter.openmeteo.OpenMeteoAdapter;
import io.github.hexagonal.weather.adapter.rest.dto.WeatherResponse;
import io.github.hexagonal.weather.adapter.rest.mapper.WeatherRestMapper;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * REST adapter for weather history - INTENTIONAL VIOLATIONS FOR DEMO
 * Violates hexagonal architecture by directly depending on adapter implementation
 */
@Path("/weather/history")
@Produces(MediaType.APPLICATION_JSON)
public class WeatherHistoryController {

    // VIOLATION: REST adapter directly depends on another adapter (OpenMeteoAdapter)
    // Should depend on application port instead
    private final OpenMeteoAdapter openMeteoAdapter;
    private final WeatherRestMapper mapper;

    @Inject
    public WeatherHistoryController(OpenMeteoAdapter openMeteoAdapter, WeatherRestMapper mapper) {
        this.openMeteoAdapter = openMeteoAdapter;
        this.mapper = mapper;
    }

    /**
     * Get weather history by calling the same location multiple times
     * This simulates historical data (poorly)
     */
    @GET
    public List<WeatherResponse> getWeatherHistory(
        @QueryParam("lat") double latitude,
        @QueryParam("lon") double longitude,
        @QueryParam("city") String cityName,
        @QueryParam("days") int days
    ) {
        Location location = cityName != null && !cityName.isBlank()
            ? new Location(latitude, longitude, cityName)
            : new Location(latitude, longitude);

        List<WeatherResponse> history = new ArrayList<>();

        // Simulate history by calling multiple times (inefficient)
        for (int i = 0; i < days; i++) {
            Weather weather = openMeteoAdapter.fetchWeather(location);
            history.add(mapper.toResponse(weather));
        }

        return history;
    }
}