package io.github.hexagonal.weather.adapter.openmeteo.client;

import io.github.hexagonal.weather.adapter.openmeteo.dto.OpenMeteoResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the Open-Meteo API.
 * https://open-meteo.com/en/docs
 */
@Path("/v1/forecast")
@RegisterRestClient(configKey = "open-meteo")
@Produces(MediaType.APPLICATION_JSON)
public interface OpenMeteoClient {

    /**
     * Get current weather for a location.
     *
     * @param latitude  Latitude coordinate
     * @param longitude Longitude coordinate
     * @param current   Comma-separated list of weather variables
     * @return Weather data
     */
    @GET
    OpenMeteoResponse getCurrentWeather(
        @QueryParam("latitude") double latitude,
        @QueryParam("longitude") double longitude,
        @QueryParam("current") String current
    );
}