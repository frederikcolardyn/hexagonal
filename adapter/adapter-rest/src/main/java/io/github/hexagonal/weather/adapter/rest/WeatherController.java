package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.adapter.rest.dto.WeatherResponse;
import io.github.hexagonal.weather.adapter.rest.mapper.WeatherRestMapper;
import io.github.hexagonal.weather.application.port.in.GetWeatherUseCase;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.Weather;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.jbosslog.JBossLog;

/**
 * REST adapter providing HTTP endpoints for weather information.
 * This is an incoming adapter that translates HTTP requests to use case calls.
 */
@Path("/weather")
@Produces(MediaType.APPLICATION_JSON)
@JBossLog
public class WeatherController {

    private final GetWeatherUseCase getWeatherUseCase;
    private final WeatherRestMapper mapper;

    @Inject
    public WeatherController(GetWeatherUseCase getWeatherUseCase, WeatherRestMapper mapper) {
        this.getWeatherUseCase = getWeatherUseCase;
        this.mapper = mapper;
    }

    /**
     * Get current weather for a location.
     *
     * @param latitude  Latitude coordinate
     * @param longitude Longitude coordinate
     * @param cityName  Optional city name
     * @return Weather information
     */
    @GET
    public WeatherResponse getWeather(
        @QueryParam("lat") double latitude,
        @QueryParam("lon") double longitude,
        @QueryParam("city") String cityName
    ) {
        log.infof("REST request: GET /weather?lat=%f&lon=%f&city=%s", latitude, longitude, cityName);

        Location location = cityName != null && !cityName.isBlank()
            ? new Location(latitude, longitude, cityName)
            : new Location(latitude, longitude);

        Weather weather = getWeatherUseCase.getWeather(location);

        return mapper.toResponse(weather);
    }
}