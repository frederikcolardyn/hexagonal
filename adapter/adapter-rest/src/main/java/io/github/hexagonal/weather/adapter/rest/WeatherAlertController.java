package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.adapter.rest.dto.WeatherAlertResponse;
import io.github.hexagonal.weather.adapter.rest.mapper.WeatherAlertRestMapper;
import io.github.hexagonal.weather.application.port.in.CheckWeatherAlertUseCase;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherAlert;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * REST controller for weather alerts.
 */
@Path("/weather/alert")
@Produces(MediaType.APPLICATION_JSON)
public class WeatherAlertController {

    private final CheckWeatherAlertUseCase useCase;
    private final WeatherAlertRestMapper mapper;

    public WeatherAlertController(CheckWeatherAlertUseCase useCase, WeatherAlertRestMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @GET
    public WeatherAlertResponse getWeatherAlert(
            @QueryParam("lat") double latitude,
            @QueryParam("lon") double longitude,
            @QueryParam("city") @DefaultValue("Unknown") String cityName) {

        Location location = new Location(latitude, longitude, cityName);
        WeatherAlert alert = useCase.checkAlert(location);

        return mapper.toResponse(alert);
    }
}
