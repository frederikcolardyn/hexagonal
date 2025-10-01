package io.github.hexagonal.weather.adapter.rest;

import io.github.hexagonal.weather.adapter.rest.dto.ForecastResponse;
import io.github.hexagonal.weather.adapter.rest.mapper.ForecastRestMapper;
import io.github.hexagonal.weather.application.port.in.GetWeatherForecastUseCase;
import io.github.hexagonal.weather.model.Location;
import io.github.hexagonal.weather.model.WeatherForecast;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.jbosslog.JBossLog;

/**
 * REST adapter providing HTTP endpoints for weather forecast information.
 * This is an incoming adapter that translates HTTP requests to use case calls.
 */
@Path("/forecast")
@Produces(MediaType.APPLICATION_JSON)
@JBossLog
public class ForecastController {

    private final GetWeatherForecastUseCase getForecastUseCase;
    private final ForecastRestMapper mapper;

    @Inject
    public ForecastController(GetWeatherForecastUseCase getForecastUseCase, ForecastRestMapper mapper) {
        this.getForecastUseCase = getForecastUseCase;
        this.mapper = mapper;
    }

    /**
     * Get weather forecast for a location.
     *
     * @param latitude  Latitude coordinate
     * @param longitude Longitude coordinate
     * @param cityName  Optional city name
     * @param days      Number of days to forecast (default: 5)
     * @return Weather forecast information
     */
    @GET
    public ForecastResponse getForecast(
        @QueryParam("lat") double latitude,
        @QueryParam("lon") double longitude,
        @QueryParam("city") String cityName,
        @QueryParam("days") @DefaultValue("5") int days
    ) {
        log.infof("REST request: GET /forecast?lat=%f&lon=%f&city=%s&days=%d", latitude, longitude, cityName, days);

        Location location = cityName != null && !cityName.isBlank()
            ? new Location(latitude, longitude, cityName)
            : new Location(latitude, longitude);

        WeatherForecast forecast = getForecastUseCase.getForecast(location, days);

        return mapper.toResponse(forecast);
    }
}
