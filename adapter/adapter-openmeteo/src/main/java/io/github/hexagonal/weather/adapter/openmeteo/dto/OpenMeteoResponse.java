package io.github.hexagonal.weather.adapter.openmeteo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing the Open-Meteo API response.
 */
public record OpenMeteoResponse(
    @JsonProperty("latitude") double latitude,
    @JsonProperty("longitude") double longitude,
    @JsonProperty("current") CurrentWeather current
) {
    public record CurrentWeather(
        @JsonProperty("time") String time,
        @JsonProperty("temperature_2m") double temperature,
        @JsonProperty("weather_code") int weatherCode
    ) {}
}